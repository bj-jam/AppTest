package com.app.test.game.adapter;


import android.graphics.Point;
import android.support.v4.content.ContextCompat;
import android.util.SparseBooleanArray;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.app.test.R;
import com.app.test.game.api.ItemTypeListener;
import com.app.test.game.api.NoDoubleClickListener;
import com.app.test.game.bean.CharacterTips;
import com.app.test.game.bean.IdiomFillState;
import com.app.test.game.bean.IdiomViewPosition;
import com.app.test.game.bean.Proverb;
import com.app.test.game.bean.ProverbCharacter;
import com.app.test.game.bean.ProverbCharacterWrapper;
import com.app.test.game.bean.ProverbDisturbMapCharacter;
import com.app.test.game.bean.SuperType;
import com.app.test.util.DensityUtil;
import com.app.test.game.helper.IdiomHelper;
import com.app.test.game.api.CheckIdiomListener;
import com.app.test.game.source.IdiomType;
import com.app.test.util.StringUtils;
import com.app.test.util.Utils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static com.app.test.game.source.IdiomType.ALL_FILL_RIGHT;
import static com.app.test.game.source.IdiomType.EACH_PROVERB_RIGHT_COLLECT;
import static com.app.test.game.source.IdiomType.EACH_PROVERB_RIGHT_COUNT;
/**
 * @author lcx
 * Created at 2020.3.27
 * Describe:
 */
public class IdiomBoardAdapter extends BaseQuickAdapter<Integer, BaseViewHolder> {
    //用来记录全部填对的成语所在下标
    private volatile SparseBooleanArray allRightProverbIndex = new SparseBooleanArray();
    private ItemTypeListener<Integer> itemTypeListener;
    private ProverbCharacterWrapper[][] proverbCharacters;
    private int column = 5;
    private int row = 4;
    private int dp3 = 0;
    private int itemWidth;
    private volatile Map<Integer, ProverbDisturbMapCharacter> characterMap = new ConcurrentHashMap<>();
    //记录点击的二维坐标
    private volatile Point selectPoint = new Point(-1, -1);
    /*上一个point坐标*/
    //手动移动红框时记录上一个二维坐标，利用两次不同的选择框判断是否在一个成语中，用于确定成语下标位置，填字时就重置
    private volatile Point prePoint;
    /*每个问题的成语集合*/
    private volatile ArrayList<Proverb> proverbList;

    /*记录填字时，所在成语对应的下标*/
    private volatile int currentProverbIndex = -1;


    /*记录开始答题的当前时间或者答对之后的时间，用于统计每次答对成语的间隔*/
    private long beforeRightTime;
    private IdiomViewPosition idiomViewPosition;

    public IdiomBoardAdapter(List<Integer> data, ProverbCharacterWrapper[][] characters, int row, int column) {
        super(R.layout.item_idiom_word_show, data);
        beforeRightTime = System.currentTimeMillis();
        this.row = row;
        this.column = column;
        this.proverbCharacters = characters;
        this.itemWidth = 0;
        allRightProverbIndex = new SparseBooleanArray();
        prePoint = new Point(-1, -1);
    }

    /*初始化数据时，默认提供一个选择框*/
    public void setFirstSelectPoint(Point point, int firstSelectPosition) {
        if (Utils.isEmpty(point)) {
            return;
        }
        int x = point.x;
        int y = point.y;
        getSelectPoint().set(x, y);
        if (Utils.isEmpty(getSourceWrapperData())) {
            return;
        }
        if (firstSelectPosition < 0) {
            if (x < 0 || y < 0) {
                return;
            }
            ProverbCharacterWrapper wrapper = getSourceWrapperData()[x][y];
            if (Utils.isEmpty(wrapper)) {
                return;
            }
            currentProverbIndex = wrapper.getProverbIndex();
        } else {
            currentProverbIndex = firstSelectPosition;
        }

    }

    public void setItemTypeListener(ItemTypeListener<Integer> itemTypeListener) {
        this.itemTypeListener = itemTypeListener;
    }

    public void setProverList(ArrayList<Proverb> list) {
        proverbList = list;
    }

    public int getCurrentProverbIndex() {
        return currentProverbIndex;
    }

    public void setCurrentProverbIndex(int currentProverbIndex) {
        this.currentProverbIndex = currentProverbIndex;
    }

    public ArrayList<Proverb> getProverbList() {
        if (Utils.isEmpty(proverbList)) {
            return new ArrayList<>();
        }
        return proverbList;
    }

    public ProverbCharacterWrapper[][] getSourceWrapperData() {
        return proverbCharacters;
    }

    private ProverbCharacter getSourceData(int x, int y) {
        if (Utils.isEmpty(getSourceWrapperData())) {
            return null;
        }
        ProverbCharacterWrapper characterWrapper = getSourceWrapperData()[x][y];
        if (Utils.isEmpty(characterWrapper)) {
            return null;
        }
        return characterWrapper.getProverbCharacter();
    }

    /*下标转二维坐标*/
    public Point indexConvert(int position) {
        int x = position / column;
        int y = position % column;
        Point point = new Point(x, y);
        return point;
    }

    /*二维坐标转下标*/
    public int pointConvert(int x, int y) {
        int index = x * column + y;
        if (index < 0) {
            index = 0;
        }
        return index;
    }


    @Override
    protected void convert(BaseViewHolder helper, final Integer integer) {
        final int position = helper.getAdapterPosition();
        View flGameProverb = helper.getView(R.id.fl_game_proverb);
        setItemWidth(flGameProverb);
        View vSelectIdiomWordTips = helper.getView(R.id.v_select_idiom_word_tips);
        final View vSelectIdiomWord = helper.getView(R.id.v_select_idiom_word);
        TextView tvSelectIdiomWord = helper.getView(R.id.tv_select_idiom_word);
        Point point = indexConvert(helper.getAdapterPosition());
        final ProverbCharacterWrapper characterWrapper = proverbCharacters[point.x][point.y];
        if (Utils.isEmpty(characterWrapper)) {
            flGameProverb.setVisibility(View.INVISIBLE);
            return;
        }
        final ProverbCharacter character = characterWrapper.getProverbCharacter();
        if (Utils.isEmpty(character)) {
            flGameProverb.setVisibility(View.INVISIBLE);
            return;
        }
        flGameProverb.setVisibility(View.VISIBLE);

        int relativeX = character.getRelativeX();
        int relativeY = character.getRelativeY();
        tvSelectIdiomWord.setTextColor(ContextCompat.getColor(tvSelectIdiomWord.getContext(), R.color.white));

        //如果红框提示与当前item一致，则显示
        isShowRedTips(vSelectIdiomWordTips, relativeX, relativeY);

        if (characterWrapper.isAllFill()) {
            /*全部填完*/
            if (characterWrapper.isAllRight()) {
                /*全部正确*/
                tvSelectIdiomWord.setText(character.getTitle());
                vSelectIdiomWord.setBackgroundResource(R.drawable.shape_idiom_success);
            } else if (character.isShow()) {
                /*全部填完但是所填词不正确，正常显示固定词*/
                tvSelectIdiomWord.setText(character.getTitle());
                vSelectIdiomWord.setBackgroundResource(R.drawable.shape_idiom_normal);
            } else if (character.isFilled()) {
                tvSelectIdiomWord.setText(character.getShortTitle());
                vSelectIdiomWord.setBackgroundResource(R.drawable.shape_idiom_select);
                tvSelectIdiomWord.setTextColor(ContextCompat.getColor(tvSelectIdiomWord.getContext(), R.color._fb4303));
            } else {
                tvSelectIdiomWord.setText("");
                vSelectIdiomWord.setBackgroundResource(R.drawable.shape_idiom_empty);
            }
        } else if (character.isShow()) {
            /*是否正常显示*/
            tvSelectIdiomWord.setText(character.getTitle());
            vSelectIdiomWord.setBackgroundResource(R.drawable.shape_idiom_normal);
        } else if (character.isFilled()) {
            /*如果不正常显示，是否填充状态*/
            //填充用户选择的词
            tvSelectIdiomWord.setText(character.getShortTitle());
            vSelectIdiomWord.setBackgroundResource(R.drawable.shape_idiom_select);
        } else {
            tvSelectIdiomWord.setText("");
            vSelectIdiomWord.setBackgroundResource(R.drawable.shape_idiom_empty);
        }


        vSelectIdiomWord.setOnClickListener(new NoDoubleClickListener() {
            @Override
            public void onNoDoubleClick(View v) {
                if (Utils.isEmpty(character) || characterWrapper.isAllRight()) {
                    return;
                }
                //如果字正常显示，点击无效果
                if (character.isShow()) {
                    return;
                }
                if (character.isFilled()) {
                    character.setFilled(false);
                    /*如果点击的是已经填充的备选字，则把字还给备选面板*/
                    takeDownWord(position);

                    listenerChangeItem(position);
                } else {
                    Point point = indexConvert(position);
                    clearPreRedTips();
                    setNextPoint(point.x, point.y);
                    listenerChangeItem(position);
                }
                /*如果手动选择红框，如果上一个X坐标或Y坐标和当前选择的X或者Y坐标一致，那么还是在当前成语中*//*
                //只有当x,y坐标都不一样的情况下，才取判断选择到哪个下标的成语
               */
                int currentProverbIndexForChar = getCurrentProverbIndexForChar(getCurrentProverbIndex(), character);
                if (currentProverbIndexForChar >= 0) {
                    setCurrentProverbIndex(currentProverbIndexForChar);
                }

            }
        });
    }

    private void clearPreRedTips() {
        if (isSelectIdiomInBoard()) {
            //如果之前有选择其他位置，那么刷新掉
            int prePosition = pointConvert(getSelectPoint().x, getSelectPoint().y);
            /*记录上一个坐标*/
            setPrePoint(getSelectPoint().x, getSelectPoint().y);
            resetNextPoint();
            listenerChangeItem(prePosition);
        }
    }

    /*如果红框提示与当前item一致，则显示*/
    private void isShowRedTips(View view, int relativeX, int relativeY) {
        if (isSelectItem(relativeX, relativeY)) {
            view.setVisibility(View.VISIBLE);
        } else {
            view.setVisibility(View.INVISIBLE);
        }
    }

    private void setItemWidth(View flGameProverb) {
        if (Utils.isEmpty(flGameProverb)) {
            return;
        }
        if (itemWidth <= 0) {
            itemWidth = IdiomHelper.get().getItemWidth(flGameProverb.getContext(), column);
        }
        if (dp3 <= 0) {
            dp3 = DensityUtil.dp2px(3);
        }
        ViewGroup.LayoutParams layoutParams = flGameProverb.getLayoutParams();
        layoutParams.width = itemWidth;
        layoutParams.height = itemWidth + dp3;
        flGameProverb.setLayoutParams(layoutParams);
    }

    /*如果点击的是已经填充的备选字，则把字还给备选面板*/
    private void takeDownWord(int position) {
        Point point = indexConvert(position);
        ProverbCharacterWrapper[][] characterWrappers = getSourceWrapperData();
        if (Utils.isEmpty(characterWrappers)) {
            return;
        }
        ProverbCharacterWrapper wrapper = characterWrappers[point.x][point.y];
        if (Utils.isEmpty(wrapper)) {
            return;
        }
        wrapper.setTakeDown();
        ProverbCharacter sourceData = getSourceData(point.x, point.y);
        if (Utils.isEmpty(sourceData)) {
            return;
        }
        sourceData.setShortTitle("");
        sourceData.setFilled(false);
        sourceData.setSelected(false);

        //如果之前有选择的，那么刷新掉
        clearPreRedTips();
        setNextPoint(point.x, point.y);

        //点击棋盘上面的字取下(手动取下)，或者选择备选词替换棋盘上面的字(一个成语填写失败的情况下)
        replaceDisturb(sourceData);
    }

    /*从棋盘取下某个词*/
    private void replaceDisturb(ProverbCharacter sourceData) {
        if (!Utils.isEmpty(itemTypeListener)) {
            SuperType removeDisturb = getNeedRetrieveFreeWord(sourceData);
            if (Utils.isEmpty(removeDisturb)) {
                return;
            }
            itemTypeListener.onItemType(-1, IdiomType.TAKE_DOWN_WORD, removeDisturb);
        }
    }

    public SuperType getNeedRetrieveFreeWord(ProverbCharacter sourceData) {
        SuperType removeDisturb = findRemoveDisturb(sourceData);
        if (Utils.isEmpty(removeDisturb)) {
            return new SuperType();
        }
        return removeDisturb;
    }

    public void notifyByXY(int x, int y) {
        int position = pointConvert(x, y);
        notifyItemChanged(position);
    }

    /*修改状态，用于自动选择下个词*/
    private void notifyDefault() {
        notifyDataSetChanged();
    }

    public synchronized void autoFillNextChar() {
        if (isSelectIdiomInBoard()) {
            //如果棋盘上面有选择空格
            int x = getSelectPoint().x;
            int y = getSelectPoint().y;
            ProverbCharacter character = getSourceData(x, y);
            if (Utils.isEmpty(character)) {
                return;
            }

            String title = character.getTitle();
            String shortTitle = character.getShortTitle();
            //提示的时候，比较填写的字的和正确的答案，如果正确就提示
            if (StringUtils.equals(title, shortTitle)) {
                getListener().onItemType(0, IdiomType.SHOW_TOAST, "当前文字填写正确");
                return;
            }

            CharacterTips tips = new CharacterTips();
            //需要填写正确的字
            tips.setTitle(title);
            if (character.isFilled()) {
                //如果提示的框中已经填了字
                //目前填入的字
                tips.setShortTitle(shortTitle);

            }

            getListener().onItemType(0, IdiomType.AUTO_FILL_CHAR, tips);
        } else {
            getListener().onItemType(0, IdiomType.SHOW_TOAST, "点击选择框之后再试");
        }
    }

    private SuperType findRemoveDisturb(ProverbCharacter character) {
        if (Utils.isEmpty(characterMap) || Utils.isEmpty(character)) {
            return null;
        }
        for (Map.Entry<Integer, ProverbDisturbMapCharacter> entry : characterMap.entrySet()) {
            ProverbDisturbMapCharacter value = entry.getValue();
            if (Utils.isEmpty(value)) {
                continue;
            }
            ProverbCharacter proverbCharacter = value.getProverbCharacter();
            if (Utils.isEmpty(proverbCharacter)) {
                continue;
            }
            if (proverbCharacter.equals(character)) {
                SuperType proverbDisturbWord = value.getProverbDisturbWord();
                if (!Utils.isEmpty(proverbDisturbWord) && !Utils.trimToEmpty(proverbDisturbWord.getTitle())) {
                    characterMap.remove(pointConvert(character.getRelativeX(), character.getRelativeY()));
                }
                return proverbDisturbWord;
            }
        }
        return null;
    }


    /*之前是否有选中空格*/
    private boolean isSelectIdiomInBoard() {
        if (Utils.isEmpty(getSelectPoint())) {
            return false;
        }
        return (getSelectPoint().x >= 0 && getSelectPoint().y >= 0);
    }

    private void putDisturb(int freeWordPosition, ProverbCharacter character, SuperType type) {
        if (Utils.isEmpty(character) || Utils.isEmpty(type) || Utils.trimToEmpty(type.getTitle())) {
            return;
        }
        ProverbDisturbMapCharacter pmc = new ProverbDisturbMapCharacter();
        pmc.setProverbCharacter(character);
        pmc.setProverbDisturbWord(type);
        pmc.setDisturb(type.getTitle());
        pmc.setProverbDisturbIndex(freeWordPosition);
        characterMap.put(pointConvert(character.getRelativeX(), character.getRelativeY()), pmc);
    }

    /* 选择其他字时，判断选择之前的成语下标，是否包含在选择之后的字所在成语的下标
      如果有包含，这继续用之前的成语下标
      如果不包含，切换当前成语下标
      */
    private int getCurrentProverbIndexForChar(int preIndex, ProverbCharacter character) {
        if (Utils.isEmpty(character)) {
            return -1;
        }
        ArrayList<SuperType> proverbList = character.getProverbRelationList();
        if (Utils.isEmpty(proverbList)) {
            return -1;
        }
        boolean isIncludeIndex = false;
        int nextProverbIndex = -1;
        for (int i = 0; i < proverbList.size(); i++) {
            SuperType type = proverbList.get(i);
            if (Utils.isEmpty(type)) {
                continue;
            }
            int index = type.getIndex();
            if (!Utils.isEmpty(getProverbList())) {
                Proverb proverb = getProverbList().get(index);
                //如果该成语没有包含该坐标下的字，则忽略
                boolean proverIncludeCharForXY = isProverIncludeCharForXY(character.getRelativeX(), character.getRelativeY(), proverb);
                if (!proverIncludeCharForXY) {
                    continue;
                }
            }
            //如果关联多个成语,如果成语已经全部正确则略过
            if (allRightProverbIndex.get(index)) {
                continue;
            }
            /*之前选择框的坐标*/
            int preX = getPrePoint().x;
            int preY = getPrePoint().y;
            //当前选择框的坐标
            int nowX = getSelectPoint().x;
            int nowY = getSelectPoint().y;
            if (preIndex == index) {

                //有可能不同位置有相同的词关联多个成语，
                //这个时候preindex可能和遍历出来的成语index一样，但实际上，选择框所在的成语是另外一个下标的成语
                //比如：满意春风  春风满意 春是交接点，但是是从第一个风手动切换到第二个成语的风，这个时候就需要借助两个字所在棋盘坐标判断了

                //如果是这种情况，继续判断不同位置相同的字所在棋盘坐标，如果不一样则continue，因为两个字的坐标不一样，那么关联的成语下标肯定不一样了
                if (preX >= 0 && preY >= 0 && nowX != preX && nowY != preY) {
                    continue;
                }
                isIncludeIndex = true;
                break;
            }
            nextProverbIndex = index;
            //如果当前字所在xy坐标和之前字的xy坐标都不一样，不管是否同字，那肯定是换成语了，那么就以此时下标对应的成语为主
            if (preX >= 0 && preY >= 0 && nowX != preX && nowY != preY) {
                break;
            }
            /*如果上一个字和当前字X坐标或者Y坐标一样,但是当前字关联多个成语，需要判断多个成语哪个成语同时包含这两个字*/
            if (preX >= 0 && preY >= 0 && (nowX == preX || nowY == preY)) {
                ArrayList<Proverb> sourceProverbList = getProverbList();
                if (Utils.isEmpty(sourceProverbList)) {
                    continue;
                }
                Proverb proverb = sourceProverbList.get(index);
                if (Utils.isEmpty(proverb)) {
                    continue;
                }
                //获取此时下标的成语
                String title = proverb.getTitle();
                if (Utils.isEmpty(title)) {
                    continue;
                }
                if (Utils.isEmpty(character.getTitle())) {
                    continue;
                }
                ProverbCharacter preSourceData = getSourceData(preX, preY);
                if (Utils.isEmpty(preSourceData)) {
                    continue;
                }
                String preTitle = preSourceData.getTitle();
                //如果成语同时包含这两个字，就以此时的成语下标为准
                if (title.indexOf(character.getTitle()) >= 0 && title.indexOf(preTitle) >= 0) {
                    break;
                }
            }
        }
        //如果包含，返回之前的下标，如果不包含，返回当前字所关联的第一个成语下标
        return isIncludeIndex ? preIndex : nextProverbIndex;
    }

    /*选择底下面板上面的备选词添加到棋盘上*/
    public void selectWordToBoard(int freeWordPosition, SuperType type) {

        if (Utils.isEmpty(getSourceWrapperData())) {
            return;
        }
        if (!isSelectIdiomInBoard()) {
            return;
        }
        ProverbCharacterWrapper[][] sourceWrapper = getSourceWrapperData();
        if (Utils.isEmpty(sourceWrapper)) {
            return;
        }
        ProverbCharacter character = getSourceData(getSelectPoint().x, getSelectPoint().y);
        if (Utils.isEmpty(character)) {
            return;
        }



        /*如果一个成语填完了，但是填入的词是错的，如果继续填写，需要把之前的词换下来*/
        replaceDisturb(character);


        character.setShortTitle(type.getTitle());
        character.setFilled(true);
        /*填词时将备选词保存起来*/
        putDisturb(freeWordPosition, character, type);


        //填入一个词之后，开始检查该词关联的所有成语是否填写完毕，如果完毕，则检查是否正确
        checkProverbIsAllFill(character);


        //检查所有成语是否答对
        boolean isAllFillAndRight = checkAllProverb();
        if (isAllFillAndRight) {
            clearPreRedTips();
        }
        notifyDefault();
        /*需要更新视图然后item依次执行放大动画*/
        if (!Utils.isEmpty(idiomViewPosition) && !Utils.isEmpty(idiomViewPosition.getEachViewPosition()) && idiomViewPosition.isAllRight()) {
            getListener().onItemType(0, IdiomType.ANSWER_RIGHT_IDIOM_VIEWS_POSITION, idiomViewPosition);
        }
        if (!Utils.isEmpty(idiomViewPosition) && !Utils.isEmpty(idiomViewPosition.getEachViewPosition()) && !idiomViewPosition.isAllRight()) {
            getListener().onItemType(0, IdiomType.ANSWER_ERROR_IDIOM_VIEWS_POSITION, idiomViewPosition);
        }
    }


    private boolean checkAllProverb() {
        boolean isAllFillAndRight = false;
        if (Utils.isEmpty(getProverbList())) {
            return false;
        }
        for (int i = 0; i < getProverbList().size(); i++) {
            if (allRightProverbIndex.get(i)) {
                isAllFillAndRight = true;
            } else {
                isAllFillAndRight = false;
                break;
            }
        }
        if (isAllFillAndRight) {
            getListener().onItemType(0, ALL_FILL_RIGHT, null);
        }
        return isAllFillAndRight;
    }


    /*某个坐标下的字所关联的成语(可能存在不同位置的相同字关联不同的成语)，判断这个成语是否包含这个坐标的字*/
    private boolean isProverIncludeCharForXY(int charX, int charY, Proverb proverb) {
        if (charX < 0 || charY < 0) {
            return false;
        }
        if (Utils.isEmpty(proverb)) {
            return false;
        }

        ArrayList<ProverbCharacter> proverbCharacterList = proverb.getProverbCharacterList();
        if (Utils.isEmpty(proverbCharacterList)) {
            return false;
        }
        for (int i = 0; i < proverbCharacterList.size(); i++) {
            ProverbCharacter character = proverbCharacterList.get(i);
            if (Utils.isEmpty(character)) {
                continue;
            }
            if (charX == character.getRelativeX() && charY == character.getRelativeY()) {
                return true;
            }
        }
        return false;
    }

    //填入一个词之后，开始检查该词关联的所有成语是否填写完毕，如果完毕，则检查是否正确
    private void checkProverbIsAllFill(ProverbCharacter character) {
        if (Utils.isEmpty(character)) {
            return;
        }
        //关联的成语
        ArrayList<SuperType> proverbRelationList = character.getProverbRelationList();
        if (Utils.isEmpty(proverbRelationList)) {
            return;
        }
        //所填词所关联的成语正确数量
        int proverbRightNum = 0;
        //记录所填词关联成语被答对的次数，并记录答对成语的最后一个字关联的view
        idiomViewPosition = new IdiomViewPosition();
//        List<Integer> viewPosition = new ArrayList<>();

        //记录关联的成语中是否至少有一个正确的
        boolean mappingProverbHasRight = false;
        //记录关联的成语中是否至少有一个填完的
        boolean mappingProverbHasAllFill = false;



        /*答对的成语需要上报耗时，同时答对两个，就上报两个*/
        StringBuilder stringBuilder = new StringBuilder();
        /*答对时记录当前时间*/
        long rightTime = System.currentTimeMillis();
        long intervalTime = rightTime - beforeRightTime;
        beforeRightTime = rightTime;

        int rightProverbIndex = -1;
        for (int i = 0; i < proverbRelationList.size(); i++) {
            SuperType proverbTemp = proverbRelationList.get(i);
            if (Utils.isEmpty(proverbTemp)) {
                continue;
            }
            //获取每个成语的下标
            int index = proverbTemp.getIndex();
            ArrayList<Proverb> sourceProverbList = getProverbList();
            //如果下标大于等于源数据的大小
            if (index >= sourceProverbList.size()) {
                continue;
            }
            if (allRightProverbIndex.get(index)) {
                //填入的字有可能关联两个成语，而其中一个成语可能被全部填对了，这个时候就不需要处理这个成语了
                continue;
            }
            //根据下标获取的成语
            Proverb proverbForIndex = sourceProverbList.get(index);
            if (Utils.isEmpty(proverbForIndex)) {
                continue;
            }
            //如果这个成语没有包含该坐标下的字，则忽略（有些题目存在不同位置具有相同字的情况）
            if (!isProverIncludeCharForXY(character.getRelativeX(), character.getRelativeY(), proverbForIndex)) {
                continue;
            }
            //获取该成语下面的所有字，然后拿这些字对应的坐标去和棋盘对应坐标上的比较，判断该成语所有的字否填完
            ArrayList<ProverbCharacter> proverbCharacterList = proverbForIndex.getProverbCharacterList();
            if (Utils.isEmpty(proverbCharacterList)) {
                continue;
            }
            IdiomFillState state = checkCharIsRightForProverb(proverbCharacterList);
            if (Utils.isEmpty(state)) {
                continue;
            }
            if (state.isAllRight()) {
                if (!mappingProverbHasRight) {
                    mappingProverbHasRight = true;
                }
                if (rightProverbIndex == -1) {
                    rightProverbIndex = index;
                }
                //可能同时有2个成语同时答完，所以不能提前break
                allRightProverbIndex.put(index, true);
                proverbRightNum = proverbRightNum + state.getRightCount();
                /*此处由于外部需要动画，所以记录每个完成成语的最后一个字所在下标*/
                idiomViewPosition = getEachViewPositionForIdiom(idiomViewPosition, proverbCharacterList);
                idiomViewPosition.setAllRight(true);
                /*获取答对的成语*/
                String title = proverbForIndex.getTitle();
                stringBuilder.append(title + ":" + intervalTime + ",");

                //如果该字还继续关联其他成语，则红框移动至所关联的成语中
//                getNextProverbForIndex(-1);
            } else if (state.isAllFill()) {
                mappingProverbHasAllFill = true;

                /*此处由于外部需要动画，所以记录每个填完成语对应的view*/

                idiomViewPosition = getEachViewPositionForIdiom(idiomViewPosition, proverbCharacterList);
                idiomViewPosition.setAllRight(false);

                //填充完但是没全部填对，此时不移动红框
                getListener().onItemType(0, IdiomType.PROVERB_FILL_ERROR, null);
            } else {
                //当前成语如果没有填充完，不应该捉急移动红框，因为有可能另外的关联成语填完了
                //所以这里不做处理，等关联的成语检查完之后统一处理
            }
        }
        /*如果填充的字所关联的成语填完且正确，则移动红框至下一个成语*/
        if (mappingProverbHasRight) {
            /*如果填的字只关联一个成语，那么下一个选择框所在位置，应该是在该完成成语中的某个字所关联的成语，如果该成语每个字所关联的成语填写正确，则找空格在最前的成语(初始化时的逻辑)*/
            /*如果填的字关联多个成语，那么下一个选择框则在所关联且未完成的成语上*/
            /*如果成语填对正确的数量和棋盘的成语数量一致，那么就不需要检查选择框的逻辑了*/
            if (Utils.isEmpty(allRightProverbIndex) || allRightProverbIndex.size() != getProverbList().size()) {
                findNextProverbForCurrentProverb(character, rightProverbIndex);
            }
        } else if (!mappingProverbHasAllFill) {
            //如果mappingProverbHasRight==false,则说明该字关联的成语，没有一个答完和答正确
            // 则根据当前成语的下标设置红色选择框坐标
            getNextProverbForIndex(getCurrentProverbIndex());
        }

        //此处判断该成语填对了几个词，用于上报
        if (proverbRightNum > 0) {
            getListener().onItemType(0, EACH_PROVERB_RIGHT_COLLECT, stringBuilder.toString());
            getListener().onItemType(0, EACH_PROVERB_RIGHT_COUNT, new Integer(proverbRightNum));
        }
        if (!Utils.isEmpty(idiomViewPosition) && !Utils.isEmpty(idiomViewPosition.getLastViewPosition()) && idiomViewPosition.isAllRight()) {
            getListener().onItemType(0, IdiomType.ANSWER_RIGHT_VIEWS_POSITION, idiomViewPosition);
        }
    }

    /*已经填完且填正确一个成语时，寻找下一个被关联的成语*/
    /*如果同时填对2个成语，currentProverbIndex则是前面那个成语所在下标*/

    /**
     * @param character           成语正确时所填的字
     * @param currentProverbIndex 正确时的下标
     */
    private void findNextProverbForCurrentProverb(ProverbCharacter character, int currentProverbIndex) {
        List<Proverb> validProverbList = new ArrayList<>();
        /*首先检查该字所在位置关联多少个成语*/
        ArrayList<SuperType> proverbRelationList = character.getProverbRelationList();
        if (Utils.isEmpty(proverbRelationList) || currentProverbIndex == -1) {
            autoFindNextProverb();
            return;
        }
        for (int i = 0; i < proverbRelationList.size(); i++) {
            SuperType proverbTemp = proverbRelationList.get(i);
            if (Utils.isEmpty(proverbTemp)) {
                continue;
            }
            //获取每个成语的下标
            int index = proverbTemp.getIndex();
            ArrayList<Proverb> sourceProverbList = getProverbList();
            //如果下标大于等于源数据的大小
            if (index >= sourceProverbList.size()) {
                continue;
            }
            if (allRightProverbIndex.get(index)) {
                //填入的字有可能关联两个成语，而其中一个成语可能被全部填对了，这个时候就不需要处理这个成语了
                continue;
            }
            //根据下标获取的成语
            Proverb proverbForIndex = sourceProverbList.get(index);
            if (Utils.isEmpty(proverbForIndex)) {
                continue;
            }
            //如果这个成语没有包含该坐标下的字，则忽略（有些题目存在不同位置具有相同字的情况）
            if (!isProverIncludeCharForXY(character.getRelativeX(), character.getRelativeY(), proverbForIndex)) {
                continue;
            }
            /*继续检测该字关联的成语是否填完*/
            IdiomFillState state = checkCharIsRightForProverb(proverbForIndex.getProverbCharacterList());
            if (state.isAllFill()) {
                continue;
            }
            validProverbList.add(proverbForIndex);
        }
        /*如果list不为空，说明所填字有关联其他还未填完的成语*/
        if (!Utils.isEmpty(validProverbList)) {
            Proverb proverb = validProverbList.get(0);
            /*获取某个成语下需要填字的坐标*/
            findNextCharForProverb(proverb.getProverbCharacterList());
            return;
        }

        /*如果成语填写正确时，所填字没有关联其他成语,那么寻找该成语所关联的成语*/
        ArrayList<Proverb> sourceProverbList = getProverbList();
        if (Utils.isEmpty(sourceProverbList)) {
            autoFindNextProverb();
            return;
        }
        Proverb fillRightProverb = sourceProverbList.get(currentProverbIndex);
        autoFindNextProverbForCurrentProverbEachCharacter(fillRightProverb);
    }

    /*根据当前完成的成语，寻找该成语每个字第一关联的成语，并将选择框定位至关联的成语空格上*/
    private void autoFindNextProverbForCurrentProverbEachCharacter(Proverb fillRightProverb) {
        if (Utils.isEmpty(fillRightProverb)) {
            autoFindNextProverb();
            return;
        }
        ArrayList<ProverbCharacter> proverbCharacterList = fillRightProverb.getProverbCharacterList();
        /*需要显示选择框的成语下标*/
        int needSelectProverbIndex = -1;
        for (ProverbCharacter character : proverbCharacterList) {
            needSelectProverbIndex = findNoAllFillProverbForCharacter(character);
            if (needSelectProverbIndex != -1) {
                break;
            }
        }
        if (needSelectProverbIndex == -1) {
            autoFindNextProverb();
            return;
        }
        /*设置成语所在下标*/
        setCurrentProverbIndex(needSelectProverbIndex);
        /*根据成语下标，设置选择框在该成语中的位置*/
        getNextProverbForIndex(needSelectProverbIndex);
    }

    /*根据某个字，找到该字所关联的成语中没填充完毕的成语所在下标*/
    private int findNoAllFillProverbForCharacter(ProverbCharacter character) {
        int index = -1;
        if (Utils.isEmpty(character)) {
            return index;
        }
        ArrayList<SuperType> proverbRelationList = character.getProverbRelationList();
        if (Utils.isEmpty(proverbRelationList)) {
            return index;
        }
        for (int i = 0; i < proverbRelationList.size(); i++) {
            SuperType type = proverbRelationList.get(i);
            if (Utils.isEmpty(type)) {
                continue;
            }
            int proverbIndex = type.getIndex();
            /*该字关联的成语填对，忽略*/
            if (allRightProverbIndex.get(proverbIndex)) {
                continue;
            }
            Proverb proverb = getProverbList().get(proverbIndex);
            if (Utils.isEmpty(proverb)) {
                continue;
            }
            /*如果该字位置不在关联的成语上(存在不同位置有相同的字，这样这个字就会关联多个成语)，忽略*/
            boolean proverIncludeCharForXY = isProverIncludeCharForXY(character.getRelativeX(), character.getRelativeY(), proverb);
            if (!proverIncludeCharForXY) {
                continue;
            }
            /*该字关联的成语没填完*/
            boolean nextCharForProverb = findNextCharForProverb(proverb.getProverbCharacterList());
            if (nextCharForProverb) {
                /*如果在该成语找到可填的空格，则结束,返回该成语下标*/
                index = proverbIndex;
                break;
            }
        }
        return index;
    }

    /*找下个空格成语*/
    private void autoFindNextProverb() {
        //如果有成语全部填对了，则遍历成语，找下个空格较少的成语
        IdiomFillState proverbFillState = IdiomHelper.get().getNextProverbIndex(getProverbList(),
                allRightProverbIndex, new CheckIdiomListener() {
                    @Override
                    public boolean currentCharacterNeedFill(int relativeX, int relativeY) {
                        ProverbCharacter sourceData = getSourceData(relativeX, relativeY);
                        if (Utils.isEmpty(sourceData)) {
                            return false;
                        }
                        if (!sourceData.isShow() && !sourceData.isFilled()) {
                            return true;
                        }
                        return false;
                    }
                });
        if (Utils.isEmpty(proverbFillState)) {
            //如果没找到,则全部成语中再找有空格的成语
            getNextProverbForIndex(-1);
        } else {
            Point point = proverbFillState.getPoint();
            if (Utils.isEmpty(point)) {
                //如果没找到,则全部成语中再找有空格的成语
                getNextProverbForIndex(-1);
            } else {
                setNextPoint(point.x, point.y);
                setCurrentProverbIndex(proverbFillState.getPosition());
            }
        }
    }

    //根据下标获取下一个成语，如果下标小于0，则重新遍历数据源的成语，从没有填充成功的成语里面取
    private void getNextProverbForIndex(int index) {
        ArrayList<Proverb> sourceProverbList = getProverbList();
        if (Utils.isEmpty(sourceProverbList)) {
            return;
        }
        //如果index不等于 -1 则代表该成语还有空格
        if (index >= 0 && index < sourceProverbList.size()) {
            Proverb proverb = sourceProverbList.get(index);
            if (Utils.isEmpty(proverb)) {
                return;
            }
            findNextCharForProverb(proverb.getProverbCharacterList());
            return;
        }

        for (int i = 0; i < sourceProverbList.size(); i++) {
            if (allRightProverbIndex.get(i)) {
                //如果该成语填充完毕和成功，则忽略
                continue;
            }
            boolean nextCharForProverb = findNextCharForProverb(sourceProverbList.get(i).getProverbCharacterList());
            //如果找到下一个红框，则不用遍历成语了
            if (nextCharForProverb) {
                setCurrentProverbIndex(i);
                break;
            }
        }

    }

    //获取某个成语下一个需要填词的point,如果找到，则返回true
    private boolean findNextCharForProverb(ArrayList<ProverbCharacter> proverbCharacterList) {
        if (Utils.isEmpty(proverbCharacterList)) {
            return false;
        }
        for (int i = 0; i < proverbCharacterList.size(); i++) {
            ProverbCharacter character = proverbCharacterList.get(i);
            if (Utils.isEmpty(character)) {
                continue;
            }
            int relativeX = character.getRelativeX();
            int relativeY = character.getRelativeY();
            ProverbCharacter sourceData = getSourceData(relativeX, relativeY);
            if (Utils.isEmpty(sourceData)) {
                continue;
            }
            if (!sourceData.isFilled() && !sourceData.isShow()) {
                setNextPoint(relativeX, relativeY);
                return true;
            }
        }
        return false;
    }

    /*获取每个成语下的view对应的下标*/
    private IdiomViewPosition getEachViewPositionForIdiom(IdiomViewPosition idiomViewPosition, ArrayList<ProverbCharacter> proverbCharacterList) {
        if (Utils.isEmpty(idiomViewPosition)) {
            idiomViewPosition = new IdiomViewPosition();
        }
        if (proverbCharacterList.size() - 1 >= 0) {
            /*记录答对成语的每个view下标*/
            List<Integer> wordViewPosition = new ArrayList<>();
            for (int j = 0; j < proverbCharacterList.size(); j++) {
                ProverbCharacter lastCharForProverb = proverbCharacterList.get(j);
                if (Utils.isEmpty(lastCharForProverb)) {
                    continue;
                }
                int relativeX = lastCharForProverb.getRelativeX();
                int relativeY = lastCharForProverb.getRelativeY();
                int lastCharForProverbViewPosition = pointConvert(relativeX, relativeY);
                if (j == (proverbCharacterList.size() - 1)) {
                    //收集答对成语的最后一个字所在下标
                    idiomViewPosition.addLastViewPosition(lastCharForProverbViewPosition);
                }
                //收集答对成语每个view所在下标
                wordViewPosition.add(lastCharForProverbViewPosition);
            }
            idiomViewPosition.addEachViewPosition(wordViewPosition);
        }
        return idiomViewPosition;
    }

    private IdiomFillState checkCharIsRightForProverb(ArrayList<ProverbCharacter> proverbCharacterList) {
        IdiomFillState state = new IdiomFillState();
        if (Utils.isEmpty(proverbCharacterList)) {
            return state;
        }
        if (Utils.isEmpty(getSourceWrapperData())) {
            return state;
        }
        //统计每个词语需要填字数量，用于对比每个成语填写正确的字数，来判断该成语是否全部正确
        int needFillCount = -1;

        //成语已经填充的数量(不管对错)
        int fillCount = 0;

        //每个成语填对的数量
        int eachProverRightCount = 0;

        //所填词所在的成语所含的每个字
        for (int j = 0; j < proverbCharacterList.size(); j++) {
            ProverbCharacter characterTemp = proverbCharacterList.get(j);
            if (Utils.isEmpty(characterTemp)) {
                continue;
            }
            int relativeX = characterTemp.getRelativeX();
            int relativeY = characterTemp.getRelativeY();

            ProverbCharacterWrapper proverbCharacterWrapper = getSourceWrapperData()[relativeX][relativeY];
            if (Utils.isEmpty(proverbCharacterWrapper)) {
                continue;
            }
            if ((proverbCharacterWrapper.isAllRight() && proverbCharacterWrapper.isAllFill())) {
                //如果该成语的一个字和另外答对的成语共用，那么这个成语不计算该字
                if (needFillCount < 0) {
                    needFillCount = 0;
                }
                continue;
            }
            ProverbCharacter proverbCharacter = getSourceData(relativeX, relativeY);
            //不需要填写的字不做判断
            if (Utils.isEmpty(proverbCharacter) || proverbCharacter.isShow()) {
                continue;
            }
            //记录的次数为该成语所需要填写词的数量
            if (needFillCount < 0) {
                needFillCount = 0;
            }
            needFillCount++;

            if (proverbCharacter.isFilled()) {
                //记录填入的数量
                fillCount++;
                if (StringUtils.equals(proverbCharacter.getTitle(), proverbCharacter.getShortTitle())) {
                    //记录填入且正确的数量
                    eachProverRightCount++;
                }
            }

        }

        //该成语是否填充完毕
        boolean allFill = false;
        //改成语是否填充正确
        boolean allRight = false;
        if (needFillCount >= 0 && needFillCount == fillCount) {
            allFill = true;
        }
        if (allFill && eachProverRightCount == fillCount) {
            allRight = true;
        }
        //循环，设置每个字所在成语是否全部填充完，和填对
        for (int j = 0; j < proverbCharacterList.size(); j++) {
            ProverbCharacter characterTemp = proverbCharacterList.get(j);
            if (Utils.isEmpty(characterTemp)) {
                continue;
            }
            int relativeX = characterTemp.getRelativeX();
            int relativeY = characterTemp.getRelativeY();

            ProverbCharacterWrapper proverbCharacterWrapper = getSourceWrapperData()[relativeX][relativeY];
            if (Utils.isEmpty(proverbCharacterWrapper) || (proverbCharacterWrapper.isAllRight() && proverbCharacterWrapper.isAllFill())) {
                //如果该成语的一个字和另外答对的成语共用，那么这个成语不计算该字
                continue;
            }
            proverbCharacterWrapper.setAllFill(allFill);
            proverbCharacterWrapper.setAllRight(allRight);

        }
        //表示是否填充完
        state.setAllFill(allFill);
        //表示是否全部填充正确
        state.setAllRight(allRight);
        //每个成语填充正确的数量
        state.setRightCount(eachProverRightCount);
        return state;
    }


    private void listenerChangeItem(int position) {
        notifyItemChanged(position);
    }

    private ItemTypeListener<Integer> getListener() {
        if (Utils.isEmpty(itemTypeListener)) {
            itemTypeListener = new ItemTypeListener<Integer>() {
                @Override
                public void onItemType(int currentIndex, Integer type, Object obj) {
                }
            };
        }
        return itemTypeListener;
    }

    public Point getPrePoint() {
        if (Utils.isEmpty(prePoint)) {
            prePoint = new Point(-1, -1);
        }
        return prePoint;
    }

    public void setPrePoint(int x, int y) {
        if (Utils.isEmpty(prePoint)) {
            prePoint = new Point(x, y);
        } else {
            prePoint.set(x, y);
        }
    }

    public Point getSelectPoint() {
        if (Utils.isEmpty(selectPoint)) {
            selectPoint = new Point(-1, -1);
        }
        return selectPoint;
    }

    public void resetNextPoint() {
        getSelectPoint().x = -1;
        getSelectPoint().y = -1;
    }

    public void setNextPoint(int x, int y) {
        if (getSelectPoint().x >= 0 && getSelectPoint().y >= 0) {
            //保存上一个框的坐标
            setPrePoint(getSelectPoint().x, getSelectPoint().y);
        }
        getSelectPoint().set(x, y);

    }

    public boolean isSelectItem(int x, int y) {
        if (getSelectPoint().x == x && getSelectPoint().y == y) {
            return true;
        }
        return false;
    }

    /*将棋盘上错误的词取下来*/
    public void resetProverb() {
        if (Utils.isEmpty(proverbCharacters)) {
            return;
        }
        int rowSize = proverbCharacters.length;
        boolean temp = false;
        for (int i = 0; i < rowSize; i++) {
            ProverbCharacterWrapper[] proverbCharacter = proverbCharacters[i];
            if (Utils.isEmpty(proverbCharacter)) {
                continue;
            }
            int columnSize = proverbCharacter.length;
            for (int j = 0; j < columnSize; j++) {
                ProverbCharacterWrapper proverbCharacterWrapper = proverbCharacter[j];
                if (Utils.isEmpty(proverbCharacterWrapper)) {
                    continue;
                }
                ProverbCharacter character = proverbCharacterWrapper.getProverbCharacter();
                if (Utils.isEmpty(character)) {
                    continue;
                }
                if (Utils.isEmpty(character) || proverbCharacterWrapper.isAllRight()) {
                    continue;
                }
                //如果字正常显示
                if (character.isShow()) {
                    continue;
                }
                if (character.isFilled()) {
                    character.setFilled(false);
                    /*如果点击的是已经填充的备选字，则把字还给备选面板*/
                    int position = pointConvert(character.getRelativeX(), character.getRelativeY());
                    takeDownWord(position);
                    listenerChangeItem(position);
                    temp = true;
                }
            }
        }
        if (temp) {
            //定位到空格最上面的成语
            autoFindNextProverb();
            notifyDefault();
        }
    }
}
