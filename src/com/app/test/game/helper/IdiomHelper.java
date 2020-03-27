package com.app.test.game.helper;

import android.content.Context;
import android.graphics.Point;
import android.util.SparseBooleanArray;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;

import com.app.test.game.api.CheckIdiomListener;
import com.app.test.game.bean.IdiomFillState;
import com.app.test.game.bean.Idiom;
import com.app.test.game.bean.IdiomWord;
import com.app.test.game.bean.Question;
import com.app.test.game.bean.SuperType;
import com.app.test.game.source.AnswerType;
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
    public IdiomFillState getNextProverbIndex(ArrayList<Idiom> list, SparseBooleanArray allRightProverbIndex,
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
            Idiom idiom = list.get(i);
            if (Utils.isEmpty(idiom) || Utils.isEmpty(idiom.getProverbCharacterList())) {
                continue;
            }
            for (int j = 0; j < idiom.getProverbCharacterList().size(); j++) {
                IdiomWord character = idiom.getProverbCharacterList().get(j);
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


    public Point getNextCharacterXY(Idiom idiom, CheckIdiomListener listener) {
        Point point = new Point(-1, -1);
        if (Utils.isEmpty(idiom)) {
            return point;
        }
        for (int j = 0; j < idiom.getProverbCharacterList().size(); j++) {
            IdiomWord character = idiom.getProverbCharacterList().get(j);
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
    public IdiomWord[][] produceTestData() {
        Question question = new Question();
        question.setQuestionType(AnswerType.PROVER);
        question.setRelativeRow(4);
        question.setRelativeColumn(5);
        ArrayList<Idiom> idiomList = new ArrayList<>();

        Idiom idiom = new Idiom();
        idiom.setTitle("聚沙成塔");
        idiom.setHorizontal(true);
        idiom.setStartIndex(39);
        idiomList.add(idiom);

        String title = idiom.getTitle();
        String[] strings = new String[]{"聚", "沙", "成", "塔"};
        Boolean[] show = new Boolean[]{true, true, false, true};
        Integer[] y = new Integer[]{0, 1, 2, 3};
        Integer[] x = new Integer[]{1, 1, 1, 1};
        ArrayList<IdiomWord> charList = getCharList(
                title,
                strings,
                show,
                x,
                y
        );
        idiom.setProverbCharacterList(charList);

        idiom = new Idiom();
        idiom.setTitle("飞沙走石");
        idiom.setHorizontal(false);
        idiom.setStartIndex(31);
        idiomList.add(idiom);
        title = idiom.getTitle();
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
        idiom.setProverbCharacterList(charList);


        idiom = new Idiom();
        idiom.setTitle("石破天惊");
        idiom.setHorizontal(true);
        idiom.setStartIndex(58);
        idiomList.add(idiom);
        title = idiom.getTitle();
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
        idiom.setProverbCharacterList(charList);

        question.setProverbList(idiomList);

        if (question == null || question.getProverbList() == null) {
            return null;
        }
        IdiomWord[][] strArray = new IdiomWord[question.getRelativeRow()][question.getRelativeColumn()];
        for (int i = 0; i < question.getProverbList().size(); i++) {
            Idiom idiom1 = question.getProverbList().get(i);
            for (int j = 0; j < idiom1.getProverbCharacterList().size(); j++) {
                IdiomWord proverbList1 = idiom1.getProverbCharacterList().get(j);
                int relativeX = proverbList1.getRelativeX();
                int relativeY = proverbList1.getRelativeY();
                strArray[relativeX][relativeY] = proverbList1;
            }
        }
        return strArray;
    }

    public ArrayList<IdiomWord> getCharList(String pro, String[] strings, Boolean[] show, Integer[] x, Integer[] y) {
        ArrayList<IdiomWord> charList = new ArrayList<>();
        if (strings == null || show == null) {
            return charList;
        }
        int length = strings.length;
        for (int i = 0; i < length; i++) {
            IdiomWord character = new IdiomWord();
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

    public void logData(IdiomWord[][] idiomWords) {
        if (idiomWords == null) {
            return;
        }
        int length = idiomWords.length;
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length; i++) {
            sb.append("stem.out: \n");
            int size = idiomWords[i].length;
            for (int j = 0; j < size; j++) {
                if (idiomWords[i][j] == null) {
                    sb.append("口");
                } else {
                    sb.append(idiomWords[i][j].getTitle() + "");
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
