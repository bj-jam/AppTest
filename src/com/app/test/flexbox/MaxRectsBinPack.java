package com.app.test.flexbox;


import java.util.List;
import java.util.Vector;


/**
 * @author lcx
 * Created at 2020.4.15
 * Describe:
 */
public class MaxRectsBinPack {
    public int binWidth = 0;
    public int binHeight = 0;
    public boolean allowRotations = false;

    public Vector<Rectangle> usedRectangles = new Vector<>();
    public Vector<Rectangle> freeRectangles = new Vector<>();

    private int score1 = 0; // Unused in this function. We don't need to know the score after finding the position.
    private int score2 = 0;
    private int bestShortSideFit;
    private int bestLongSideFit;

    public MaxRectsBinPack(int width, int height, boolean rotations) {
        init(width, height, rotations);
    }


    private void init(int width, int height, boolean rotations) {
//        if (count(width) % 1 != 0 || count(height) % 1 != 0)
//            throw new Error("Must be 2,4,8,16,32,...512,1024,...");
        binWidth = width;
        binHeight = height;
        allowRotations = rotations;

        Rectangle n = new Rectangle();
        n.x = 0;
        n.y = 0;
        n.width = width;
        n.height = height;

        usedRectangles.clear();

        freeRectangles.clear();
        freeRectangles.add(n);
    }

//    private int count(int n) {
//        if (n >= 2)
//            return count(n / 2);
//        return n;
//    }

    /**
     * Insert a new Rectangle
     *
     * @param width
     * @param height
     * @param method
     * @return
     */
    public Rectangle insert(int width, int height, int method) {
        Rectangle newNode = new Rectangle();
        score1 = 0;
        score2 = 0;
        switch (method) {
            case FreeRectangleChoiceHeuristic.BestShortSideFit:
                newNode = findPositionForNewNodeBestShortSideFit(width, height);
                break;
            case FreeRectangleChoiceHeuristic.BottomLeftRule:
                newNode = findPositionForNewNodeBottomLeft(width, height, score1, score2);
                break;
            case FreeRectangleChoiceHeuristic.ContactPointRule:
                newNode = findPositionForNewNodeContactPoint(width, height, score1);
                break;
            case FreeRectangleChoiceHeuristic.BestLongSideFit:
                newNode = findPositionForNewNodeBestLongSideFit(width, height, score2, score1);
                break;
            case FreeRectangleChoiceHeuristic.BestAreaFit:
                newNode = findPositionForNewNodeBestAreaFit(width, height, score1, score2);
                break;
        }

        if (newNode.height == 0)
            return newNode;

        placeRectangle(newNode);
//        trace(newNode);
        return newNode;
    }

    private void insert2(List<Rectangle> Rectangles, List<Rectangle> dst, int method) {
        dst.clear();
        while (Rectangles.size() > 0) {
            int bestScore1 = Integer.MAX_VALUE;
            int bestScore2 = Integer.MAX_VALUE;
            int bestRectangleIndex = -1;
            Rectangle bestNode = new Rectangle();

            for (int i = 0; i < Rectangles.size(); ++i) {
                int score1 = 0;
                int score2 = 0;
                Rectangle newNode = scoreRectangle(Rectangles.get(i).width, Rectangles.get(i).height, method, score1, score2);

                if (score1 < bestScore1 || (score1 == bestScore1 && score2 < bestScore2)) {
                    bestScore1 = score1;
                    bestScore2 = score2;
                    bestNode = newNode;
                    bestRectangleIndex = i;
                }
            }

            if (bestRectangleIndex == -1)
                return;

            placeRectangle(bestNode);
            Rectangles.remove(bestRectangleIndex);
        }
    }

    private void placeRectangle(Rectangle node) {
        int numRectanglesToProcess = freeRectangles.size();
        for (int i = 0; i < numRectanglesToProcess; i++) {
            if (splitFreeNode(freeRectangles.get(i), node)) {
                freeRectangles.remove(i);
                --i;
                --numRectanglesToProcess;
            }
        }

        pruneFreeList();
        usedRectangles.add(node);
    }

    private Rectangle scoreRectangle(int width, int height, int method, int score1, int score2) {
        Rectangle newNode = new Rectangle();
        score1 = Integer.MAX_VALUE;
        score2 = Integer.MAX_VALUE;
        switch (method) {
            case FreeRectangleChoiceHeuristic.BestShortSideFit:
                newNode = findPositionForNewNodeBestShortSideFit(width, height);
                break;
            case FreeRectangleChoiceHeuristic.BottomLeftRule:
                newNode = findPositionForNewNodeBottomLeft(width, height, score1, score2);
                break;
            case FreeRectangleChoiceHeuristic.ContactPointRule:
                newNode = findPositionForNewNodeContactPoint(width, height, score1);
                // todo: reverse
                score1 = -score1; // Reverse since we are minimizing, but for contact point score bigger is better.
                break;
            case FreeRectangleChoiceHeuristic.BestLongSideFit:
                newNode = findPositionForNewNodeBestLongSideFit(width, height, score2, score1);
                break;
            case FreeRectangleChoiceHeuristic.BestAreaFit:
                newNode = findPositionForNewNodeBestAreaFit(width, height, score1, score2);
                break;
        }

        // Cannot fit the current Rectangle.
        if (newNode.height == 0) {
            score1 = Integer.MAX_VALUE;
            score2 = Integer.MAX_VALUE;
        }

        return newNode;
    }

    /// Computes the ratio of used surface area.
    private float occupancy() {
        float usedSurfaceArea = 0;
        for (int i = 0; i < usedRectangles.size(); i++)
            usedSurfaceArea += usedRectangles.get(i).width * usedRectangles.get(i).height;

        return usedSurfaceArea / (binWidth * binHeight);
    }

    private Rectangle findPositionForNewNodeBottomLeft(int width, int height, int bestY, int bestX) {
        Rectangle bestNode = new Rectangle();
        //memset(bestNode, 0, sizeof(Rectangle));

        bestY = Integer.MAX_VALUE;
        Rectangle rect;
        int topSideY;
        for (int i = 0; i < freeRectangles.size(); i++) {
            rect = freeRectangles.get(i);
            // Try to place the Rectangle in upright (non-flipped) orientation.
            if (rect.width >= width && rect.height >= height) {
                topSideY = rect.y + height;
                if (topSideY < bestY || (topSideY == bestY && rect.x < bestX)) {
                    bestNode.x = rect.x;
                    bestNode.y = rect.y;
                    bestNode.width = width;
                    bestNode.height = height;
                    bestY = topSideY;
                    bestX = rect.x;
                }
            }
            if (allowRotations && rect.width >= height && rect.height >= width) {
                topSideY = rect.y + width;
                if (topSideY < bestY || (topSideY == bestY && rect.x < bestX)) {
                    bestNode.x = rect.x;
                    bestNode.y = rect.y;
                    bestNode.width = height;
                    bestNode.height = width;
                    bestY = topSideY;
                    bestX = rect.x;
                }
            }
        }
        return bestNode;
    }

    private Rectangle findPositionForNewNodeBestShortSideFit(int width, int height) {
        Rectangle bestNode = new Rectangle();
        //memset(&bestNode, 0, sizeof(Rectangle));

        bestShortSideFit = Integer.MAX_VALUE;
        bestLongSideFit = score2;
        Rectangle rect;
        int leftoverHoriz;
        int leftoverVert;
        int shortSideFit;
        int longSideFit;

        for (int i = 0; i < freeRectangles.size(); i++) {
            rect = freeRectangles.get(i);
            // Try to place the Rectangle in upright (non-flipped) orientation.
            if (rect.width >= width && rect.height >= height) {
                leftoverHoriz = Math.abs(rect.width - width);
                leftoverVert = Math.abs(rect.height - height);
                shortSideFit = Math.min(leftoverHoriz, leftoverVert);
                longSideFit = Math.max(leftoverHoriz, leftoverVert);

                if (shortSideFit < bestShortSideFit || (shortSideFit == bestShortSideFit && longSideFit < bestLongSideFit)) {
                    bestNode.x = rect.x;
                    bestNode.y = rect.y;
                    bestNode.width = width;
                    bestNode.height = height;
                    bestShortSideFit = shortSideFit;
                    bestLongSideFit = longSideFit;
                }
            }
            int flippedLeftoverHoriz;
            int flippedLeftoverVert;
            int flippedShortSideFit;
            int flippedLongSideFit;
            if (allowRotations && rect.width >= height && rect.height >= width) {
                flippedLeftoverHoriz = Math.abs(rect.width - height);
                flippedLeftoverVert = Math.abs(rect.height - width);
                flippedShortSideFit = Math.min(flippedLeftoverHoriz, flippedLeftoverVert);
                flippedLongSideFit = Math.max(flippedLeftoverHoriz, flippedLeftoverVert);

                if (flippedShortSideFit < bestShortSideFit || (flippedShortSideFit == bestShortSideFit && flippedLongSideFit < bestLongSideFit)) {
                    bestNode.x = rect.x;
                    bestNode.y = rect.y;
                    bestNode.width = height;
                    bestNode.height = width;
                    bestShortSideFit = flippedShortSideFit;
                    bestLongSideFit = flippedLongSideFit;
                }
            }
        }

        return bestNode;
    }

    private Rectangle findPositionForNewNodeBestLongSideFit(int width, int height, int bestShortSideFit, int bestLongSideFit) {
        Rectangle bestNode = new Rectangle();
        bestLongSideFit = Integer.MAX_VALUE;
        Rectangle rect;

        int leftoverHoriz;
        int leftoverVert;
        int shortSideFit;
        int longSideFit;
        for (int i = 0; i < freeRectangles.size(); i++) {
            rect = freeRectangles.get(i);
            // Try to place the Rectangle in upright (non-flipped) orientation.
            if (rect.width >= width && rect.height >= height) {
                leftoverHoriz = Math.abs(rect.width - width);
                leftoverVert = Math.abs(rect.height - height);
                shortSideFit = Math.min(leftoverHoriz, leftoverVert);
                longSideFit = Math.max(leftoverHoriz, leftoverVert);

                if (longSideFit < bestLongSideFit || (longSideFit == bestLongSideFit && shortSideFit < bestShortSideFit)) {
                    bestNode.x = rect.x;
                    bestNode.y = rect.y;
                    bestNode.width = width;
                    bestNode.height = height;
                    bestShortSideFit = shortSideFit;
                    bestLongSideFit = longSideFit;
                }
            }

            if (allowRotations && rect.width >= height && rect.height >= width) {
                leftoverHoriz = Math.abs(rect.width - height);
                leftoverVert = Math.abs(rect.height - width);
                shortSideFit = Math.min(leftoverHoriz, leftoverVert);
                longSideFit = Math.max(leftoverHoriz, leftoverVert);

                if (longSideFit < bestLongSideFit || (longSideFit == bestLongSideFit && shortSideFit < bestShortSideFit)) {
                    bestNode.x = rect.x;
                    bestNode.y = rect.y;
                    bestNode.width = height;
                    bestNode.height = width;
                    bestShortSideFit = shortSideFit;
                    bestLongSideFit = longSideFit;
                }
            }
        }
//        trace(bestNode);
        return bestNode;
    }

    private Rectangle findPositionForNewNodeBestAreaFit(int width, int height, int bestAreaFit, int bestShortSideFit) {
        Rectangle bestNode = new Rectangle();
        //memset(&bestNode, 0, sizeof(Rectangle));

        bestAreaFit = Integer.MAX_VALUE;

        Rectangle rect;

        int leftoverHoriz;
        int leftoverVert;
        int shortSideFit;
        int areaFit;

        for (int i = 0; i < freeRectangles.size(); i++) {
            rect = freeRectangles.get(i);
            areaFit = rect.width * rect.height - width * height;

            // Try to place the Rectangle in upright (non-flipped) orientation.
            if (rect.width >= width && rect.height >= height) {
                leftoverHoriz = Math.abs(rect.width - width);
                leftoverVert = Math.abs(rect.height - height);
                shortSideFit = Math.min(leftoverHoriz, leftoverVert);

                if (areaFit < bestAreaFit || (areaFit == bestAreaFit && shortSideFit < bestShortSideFit)) {
                    bestNode.x = rect.x;
                    bestNode.y = rect.y;
                    bestNode.width = width;
                    bestNode.height = height;
                    bestShortSideFit = shortSideFit;
                    bestAreaFit = areaFit;
                }
            }

            if (allowRotations && rect.width >= height && rect.height >= width) {
                leftoverHoriz = Math.abs(rect.width - height);
                leftoverVert = Math.abs(rect.height - width);
                shortSideFit = Math.min(leftoverHoriz, leftoverVert);

                if (areaFit < bestAreaFit || (areaFit == bestAreaFit && shortSideFit < bestShortSideFit)) {
                    bestNode.x = rect.x;
                    bestNode.y = rect.y;
                    bestNode.width = height;
                    bestNode.height = width;
                    bestShortSideFit = shortSideFit;
                    bestAreaFit = areaFit;
                }
            }
        }
        return bestNode;
    }

    /// Returns 0 if the two intervals i1 and i2 are disjoint, or the length of their overlap otherwise.
    private int commonIntervalLength(int i1start, int i1end, int i2start, int i2end) {
        if (i1end < i2start || i2end < i1start)
            return 0;
        return Math.min(i1end, i2end) - Math.max(i1start, i2start);
    }

    private int contactPointScoreNode(int x, int y, int width, int height) {
        int score = 0;

        if (x == 0 || x + width == binWidth)
            score += height;
        if (y == 0 || y + height == binHeight)
            score += width;
        Rectangle rect;
        for (int i = 0; i < usedRectangles.size(); i++) {
            rect = usedRectangles.get(i);
            if (rect.x == x + width || rect.x + rect.width == x)
                score += commonIntervalLength(rect.y, rect.y + rect.height, y, y + height);
            if (rect.y == y + height || rect.y + rect.height == y)
                score += commonIntervalLength(rect.x, rect.x + rect.width, x, x + width);
        }
        return score;
    }

    private Rectangle findPositionForNewNodeContactPoint(int width, int height, int bestContactScore) {
        Rectangle bestNode = new Rectangle();
        //memset(&bestNode, 0, sizeof(Rectangle));

        bestContactScore = -1;

        Rectangle rect;
        int score;
        for (int i = 0; i < freeRectangles.size(); i++) {
            rect = freeRectangles.get(i);
            // Try to place the Rectangle in upright (non-flipped) orientation.
            if (rect.width >= width && rect.height >= height) {
                score = contactPointScoreNode(rect.x, rect.y, width, height);
                if (score > bestContactScore) {
                    bestNode.x = rect.x;
                    bestNode.y = rect.y;
                    bestNode.width = width;
                    bestNode.height = height;
                    bestContactScore = score;
                }
            }
            if (allowRotations && rect.width >= height && rect.height >= width) {
                score = contactPointScoreNode(rect.x, rect.y, height, width);
                if (score > bestContactScore) {
                    bestNode.x = rect.x;
                    bestNode.y = rect.y;
                    bestNode.width = height;
                    bestNode.height = width;
                    bestContactScore = score;
                }
            }
        }
        return bestNode;
    }

    private boolean splitFreeNode(Rectangle freeNode, Rectangle usedNode) {
        // Test with SAT if the Rectangles even intersect.
        if (usedNode.x >= freeNode.x + freeNode.width || usedNode.x + usedNode.width <= freeNode.x ||
                usedNode.y >= freeNode.y + freeNode.height || usedNode.y + usedNode.height <= freeNode.y)
            return false;
        Rectangle newNode;
        if (usedNode.x < freeNode.x + freeNode.width && usedNode.x + usedNode.width > freeNode.x) {
            // New node at the top side of the used node.
            if (usedNode.y > freeNode.y && usedNode.y < freeNode.y + freeNode.height) {
                newNode = freeNode.clone();
                newNode.height = usedNode.y - newNode.y;
                freeRectangles.add(newNode);
            }

            // New node at the bottom side of the used node.
            if (usedNode.y + usedNode.height < freeNode.y + freeNode.height) {
                newNode = freeNode.clone();
                newNode.y = usedNode.y + usedNode.height;
                newNode.height = freeNode.y + freeNode.height - (usedNode.y + usedNode.height);
                freeRectangles.add(newNode);
            }
        }

        if (usedNode.y < freeNode.y + freeNode.height && usedNode.y + usedNode.height > freeNode.y) {
            // New node at the left side of the used node.
            if (usedNode.x > freeNode.x && usedNode.x < freeNode.x + freeNode.width) {
                newNode = freeNode.clone();
                newNode.width = usedNode.x - newNode.x;
                freeRectangles.add(newNode);
            }

            // New node at the right side of the used node.
            if (usedNode.x + usedNode.width < freeNode.x + freeNode.width) {
                newNode = freeNode.clone();
                newNode.x = usedNode.x + usedNode.width;
                newNode.width = freeNode.x + freeNode.width - (usedNode.x + usedNode.width);
                freeRectangles.add(newNode);
            }
        }

        return true;
    }

    private void pruneFreeList() {
        for (int i = 0; i < freeRectangles.size(); i++)
            for (int j = i + 1; j < freeRectangles.size(); j++) {
                if (isContainedIn(freeRectangles.get(i), freeRectangles.get(j))) {
                    freeRectangles.remove(i);
                    --i;
                    break;
                }
                if (isContainedIn(freeRectangles.get(j), freeRectangles.get(i))) {
                    freeRectangles.remove(j);
                    --j;
                }
            }
    }

    private boolean isContainedIn(Rectangle a, Rectangle b) {
        return a.x >= b.x && a.y >= b.y
                && a.x + a.width <= b.x + b.width
                && a.y + a.height <= b.y + b.height;
    }

    public static class FreeRectangleChoiceHeuristic {
        public static final int BestShortSideFit = 0; ///< -BSSF: Positions the Rectangle against the short side of a free Rectangle into which it fits the best.
        public static final int BestLongSideFit = 1; ///< -BLSF: Positions the Rectangle against the long side of a free Rectangle into which it fits the best.
        public static final int BestAreaFit = 2; ///< -BAF: Positions the Rectangle into the smallest free Rectangle into which it fits.
        public static final int BottomLeftRule = 3; ///< -BL: Does the Tetris placement.
        public static final int ContactPointRule = 4; ///< -CP: Choosest the placement where the Rectangle touches other Rectangles as much as possible.
    }
}



