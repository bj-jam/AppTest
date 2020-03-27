package com.app.test.game.helper;

import android.app.Activity;
import android.content.Context;
import android.graphics.Point;
import android.support.v4.util.SparseArrayCompat;
import android.util.SparseBooleanArray;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;

import com.app.test.game.api.CheckIdiomListener;
import com.app.test.game.bean.IdiomFillState;
import com.app.test.game.bean.Proverb;
import com.app.test.game.bean.ProverbCharacter;
import com.app.test.game.bean.Question;
import com.app.test.game.bean.SuperType;
import com.app.test.game.source.AnswerType;
import com.app.test.util.ContextUtils;
import com.app.test.util.DensityUtil;
import com.app.test.util.Utils;

import java.util.ArrayList;
import java.util.List;

public class IdiomHelper {
    private static IdiomHelper singleObj;

    private IdiomHelper() {
    }

    public static IdiomHelper get() {
        if (singleObj == null) {
            synchronized (IdiomHelper.class) {
                if (singleObj == null) {
                    singleObj = new IdiomHelper();
                }
            }
        }
        return singleObj;
    }

    /*获取需要填空的下一个成语(空格从上到下的原则)取最上面空格所在成语的所在下标*/
    public IdiomFillState getNextProverbIndex(ArrayList<Proverb> list, SparseBooleanArray allRightProverbIndex,
                                              CheckIdiomListener listener) {
        if (Utils.isEmpty(list) || Utils.isEmpty(listener)) {
            return null;
        }
        if (Utils.isEmpty(allRightProverbIndex)) {
            allRightProverbIndex = new SparseBooleanArray();
        }
        /*记录需要选择的成语所在下标，以及选择框所在坐标*/
        Point point = new Point(-1, -1);
        int nextPosition = -1;
        int tempX = 1000, tempY = 1000;
        IdiomFillState state = new IdiomFillState();
        //需要寻找空格最少的一个成语 ，将红色指示框定位在该成语下面
        for (int i = 0; i < list.size(); i++) {
            //如果某个成语全部填写正确，则判断下一个
            if (allRightProverbIndex.get(i)) {
                continue;
            }
            Proverb proverb = list.get(i);
            if (Utils.isEmpty(proverb) || Utils.isEmpty(proverb.getProverbCharacterList())) {
                continue;
            }
            for (int j = 0; j < proverb.getProverbCharacterList().size(); j++) {
                ProverbCharacter character = proverb.getProverbCharacterList().get(j);
                if (Utils.isEmpty(character)) {
                    continue;
                }

                int relativeX = character.getRelativeX();
                int relativeY = character.getRelativeY();
                boolean needFill = listener.currentCharacterNeedFill(relativeX, relativeY);

                if (needFill) {
                    /*记录进入答题第一个选择的成语所在下标，以及选择框所在坐标,所以需要比较哪个最小*/
                    if ((tempX > relativeX) || (tempX == relativeX && tempY >= relativeY)) {
                        tempX = relativeX;
                        tempY = relativeY;
                        point = new Point(relativeX, relativeY);
                        nextPosition = i;
                        state.setPoint(point);
                        state.setPosition(nextPosition);
                    }
                }
            }
        }
        if (nextPosition == -1) {
            return null;
        }
        return state;
    }


    public Point getNextCharacterXY(Proverb proverb, CheckIdiomListener listener) {
        Point point = new Point(-1, -1);
        if (Utils.isEmpty(proverb)) {
            return point;
        }
        for (int j = 0; j < proverb.getProverbCharacterList().size(); j++) {
            ProverbCharacter character = proverb.getProverbCharacterList().get(j);
            if (Utils.isEmpty(character)) {
                continue;
            }
            int relativeX = character.getRelativeX();
            int relativeY = character.getRelativeY();
            boolean needFill = listener.currentCharacterNeedFill(relativeX, relativeY);

            if (needFill) {
                point.set(relativeX, relativeY);
                return point;
            }
        }
        return point;
    }


    private SparseArrayCompat<View> array = new SparseArrayCompat();

    public void removeTempView(Context context) {
        Activity activity = ContextUtils.findActivity(context);
        if (Utils.isEmpty(activity)) {
            return;
        }
        if (Utils.isEmpty(array)) {
            return;
        }
        ViewGroup contentView = activity.getWindow().getDecorView().findViewById(android.R.id.content);
        for (int i = 0; i < array.size(); i++) {
            contentView.removeView(array.valueAt(i));
        }
        array.clear();
    }


    public int getItemWidth(Context context, int column) {
        int ignore = 15 * 2 + 5 * 2 + column;
        int screenWidth = DensityUtil.getScreenWidth(context);
        int itemWidth = (screenWidth - ignore) / column;
        itemWidth = Math.min(itemWidth, DensityUtil.dp2px(40));
        if (itemWidth <= 0) {
            itemWidth = DensityUtil.dp2px(40);
        }
        return itemWidth;
    }

    /*生成测试数据*/
    public ProverbCharacter[][] produceTestData() {
        Question question = new Question();
        question.setQuestionType(AnswerType.PROVER);
        question.setRelativeRow(4);
        question.setRelativeColumn(5);
        ArrayList<Proverb> proverbList = new ArrayList<>();

        Proverb proverb = new Proverb();
        proverb.setTitle("聚沙成塔");
        proverb.setHorizontal(true);
        proverb.setStartIndex(39);
        proverbList.add(proverb);

        String title = proverb.getTitle();
        String[] strings = new String[]{"聚", "沙", "成", "塔"};
        Boolean[] show = new Boolean[]{true, true, false, true};
        Integer[] y = new Integer[]{0, 1, 2, 3};
        Integer[] x = new Integer[]{1, 1, 1, 1};
        ArrayList<ProverbCharacter> charList = getCharList(
                title,
                strings,
                show,
                x,
                y
        );
        proverb.setProverbCharacterList(charList);

        proverb = new Proverb();
        proverb.setTitle("飞沙走石");
        proverb.setHorizontal(false);
        proverb.setStartIndex(31);
        proverbList.add(proverb);
        title = proverb.getTitle();
        strings = new String[]{"飞", "沙", "走", "石"};
        show = new Boolean[]{true, true, true, false};
        y = new Integer[]{1, 1, 1, 1};
        x = new Integer[]{0, 1, 2, 3};
        charList = getCharList(
                title,
                strings,
                show,
                x,
                y
        );
        proverb.setProverbCharacterList(charList);


        proverb = new Proverb();
        proverb.setTitle("石破天惊");
        proverb.setHorizontal(true);
        proverb.setStartIndex(58);
        proverbList.add(proverb);
        title = proverb.getTitle();
        strings = new String[]{"石", "破", "天", "惊"};
        show = new Boolean[]{false, true, false, true};
        y = new Integer[]{1, 2, 3, 4};
        x = new Integer[]{3, 3, 3, 3};
        charList = getCharList(
                title,
                strings,
                show,
                x,
                y
        );
        proverb.setProverbCharacterList(charList);

        question.setProverbList(proverbList);

        if (question == null || question.getProverbList() == null) {
            return null;
        }
        ProverbCharacter[][] strArray = new ProverbCharacter[question.getRelativeRow()][question.getRelativeColumn()];
        for (int i = 0; i < question.getProverbList().size(); i++) {
            Proverb proverb1 = question.getProverbList().get(i);
            for (int j = 0; j < proverb1.getProverbCharacterList().size(); j++) {
                ProverbCharacter proverbList1 = proverb1.getProverbCharacterList().get(j);
                int relativeX = proverbList1.getRelativeX();
                int relativeY = proverbList1.getRelativeY();
                strArray[relativeX][relativeY] = proverbList1;
            }
        }
        return strArray;
    }

    public ArrayList<ProverbCharacter> getCharList(String pro, String[] strings, Boolean[] show, Integer[] x, Integer[] y) {
        ArrayList<ProverbCharacter> charList = new ArrayList<>();
        if (strings == null || show == null) {
            return charList;
        }
        int length = strings.length;
        for (int i = 0; i < length; i++) {
            ProverbCharacter character = new ProverbCharacter();
            character.setTitle(strings[i]);
            character.setShow(show[i]);
//            character.setTitle(pro);
            character.setRelativeX(x[i]);
            character.setRelativeY(y[i]);
//            character.setRawX(rawX[i]);
//            character.setRawY(rawY[i]);


            ArrayList<SuperType> proverbRelationList = new ArrayList<>();
            if ("沙".equals(strings[i])) {
                proverbRelationList.add(getType("聚沙成塔"));
                proverbRelationList.add(getType("飞沙走石"));
            } else if ("石".equals(strings[i])) {
                proverbRelationList.add(getType("飞沙走石"));
                proverbRelationList.add(getType("石破天惊"));
            } else {
                proverbRelationList.add(getType(pro));
            }
            character.setProverbRelationList(proverbRelationList);
            character.setShow(show[i]);
            charList.add(character);
        }
        return charList;
    }

    public SuperType getType(String str) {
        SuperType type = new SuperType();
        type.setTitle(str);
        return type;
    }

    public void logData(ProverbCharacter[][] proverbCharacters) {
        if (proverbCharacters == null) {
            return;
        }
        int length = proverbCharacters.length;
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length; i++) {
            sb.append("stem.out: \n");
            int size = proverbCharacters[i].length;
            for (int j = 0; j < size; j++) {
                if (proverbCharacters[i][j] == null) {
                    sb.append("口");
                } else {
                    sb.append(proverbCharacters[i][j].getTitle() + "");
                }
            }
        }
        System.out.println(sb.toString());
    }

    public void playRightAnim(List<View> viewList) {
        if (Utils.isEmpty(viewList)) {
            return;
        }
        for (int i = 0; i < viewList.size(); i++) {
            View view = viewList.get(i);
            playRightAnim(view);
        }
    }

    public void playRightAnim(View view) {
        if (Utils.isEmpty(view)) {
            return;
        }
        if (view.getVisibility() != View.VISIBLE) {
            return;
        }
        Animation animation = new ScaleAnimation(1, 1.2f, 1, 1.2f, ScaleAnimation.RELATIVE_TO_SELF, 0.5f, ScaleAnimation.RELATIVE_TO_SELF, 0.5f);
        animation.setRepeatMode(ScaleAnimation.REVERSE);
        animation.setRepeatCount(1);
        animation.setDuration(200);
        animation.setInterpolator(new LinearInterpolator());
        view.startAnimation(animation);
    }

    public void playErrorAnim(List<View> viewList) {
        if (Utils.isEmpty(viewList)) {
            return;
        }
        for (int i = 0; i < viewList.size(); i++) {
            View view = viewList.get(i);
            playErrorAnim(view);
        }
    }

    public void playErrorAnim(View view) {
        if (Utils.isEmpty(view)) {
            return;
        }
        Animation animation = new TranslateAnimation(ScaleAnimation.RELATIVE_TO_SELF, 0, ScaleAnimation.RELATIVE_TO_SELF, 0.11f, ScaleAnimation.RELATIVE_TO_SELF, 0, ScaleAnimation.RELATIVE_TO_SELF, 0);
        animation.setRepeatMode(ScaleAnimation.REVERSE);
        animation.setRepeatCount(1);
        animation.setDuration(130);
        animation.setInterpolator(new LinearInterpolator());
        view.startAnimation(animation);
    }


}
