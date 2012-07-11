/* ====================================================================
   Licensed to the Apache Software Foundation (ASF) under one or more
   contributor license agreements.  See the NOTICE file distributed with
   this work for additional information regarding copyright ownership.
   The ASF licenses this file to You under the Apache License, Version 2.0
   (the "License"); you may not use this file except in compliance with
   the License.  You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
==================================================================== */

package org.apache.poi.hssf.usermodel;

import org.apache.poi.ddf.EscherClientAnchorRecord;
import org.apache.poi.ddf.EscherRecord;
import org.apache.poi.ss.usermodel.ClientAnchor;

/**
 * A client anchor is attached to an excel worksheet.  It anchors against a
 * top-left and buttom-right cell.
 *
 * @author Glen Stampoultzis (glens at apache.org)
 */
public final class HSSFClientAnchor extends HSSFAnchor implements ClientAnchor {

    private EscherClientAnchorRecord _escherClientAnchor;

    public HSSFClientAnchor(EscherClientAnchorRecord escherClientAnchorRecord) {
        this._escherClientAnchor = escherClientAnchorRecord;
    }

    /**
     * Creates a new client anchor and defaults all the anchor positions to 0.
     */
    public HSSFClientAnchor() {
    }

    /**
     * Creates a new client anchor and sets the top-left and bottom-right
     * coordinates of the anchor.
     *
     * @param dx1  the x coordinate within the first cell.
     * @param dy1  the y coordinate within the first cell.
     * @param dx2  the x coordinate within the second cell.
     * @param dy2  the y coordinate within the second cell.
     * @param col1 the column (0 based) of the first cell.
     * @param row1 the row (0 based) of the first cell.
     * @param col2 the column (0 based) of the second cell.
     * @param row2 the row (0 based) of the second cell.
     */
    public HSSFClientAnchor(int dx1, int dy1, int dx2, int dy2, short col1, int row1, short col2, int row2) {
        super(dx1, dy1, dx2, dy2);

        checkRange(dx1, 0, 1023, "dx1");
        checkRange(dx2, 0, 1023, "dx2");
        checkRange(dy1, 0, 255, "dy1");
        checkRange(dy2, 0, 255, "dy2");
        checkRange(col1, 0, 255, "col1");
        checkRange(col2, 0, 255, "col2");
        checkRange(row1, 0, 255 * 256, "row1");
        checkRange(row2, 0, 255 * 256, "row2");

        setCol1((short) Math.min(col1, col2));
        setCol2((short) Math.max(col1, col2));
        setRow1((short) Math.min(row1, row2));
        setRow2((short) Math.max(row1, row2));

        if (col1 > col2){
            _isHorizontallyFlipped = true;
        }
        if (row1 > row2){
            _isVerticallyFlipped = true;
        }
    }

    /**
     * Calculates the height of a client anchor in points.
     *
     * @param sheet the sheet the anchor will be attached to
     * @return the shape height.
     */
    public float getAnchorHeightInPoints(HSSFSheet sheet) {
        int y1 = getDy1();
        int y2 = getDy2();
        int row1 = Math.min(getRow1(), getRow2());
        int row2 = Math.max(getRow1(), getRow2());

        float points = 0;
        if (row1 == row2) {
            points = ((y2 - y1) / 256.0f) * getRowHeightInPoints(sheet, row2);
        } else {
            points += ((256.0f - y1) / 256.0f) * getRowHeightInPoints(sheet, row1);
            for (int i = row1 + 1; i < row2; i++) {
                points += getRowHeightInPoints(sheet, i);
            }
            points += (y2 / 256.0f) * getRowHeightInPoints(sheet, row2);
        }

        return points;
    }

    private float getRowHeightInPoints(HSSFSheet sheet, int rowNum) {
        HSSFRow row = sheet.getRow(rowNum);
        if (row == null) {
            return sheet.getDefaultRowHeightInPoints();
        }
        return row.getHeightInPoints();
    }

    public short getCol1() {
        return _escherClientAnchor.getCol1();
    }

    public void setCol1(short col1) {
        checkRange(col1, 0, 255, "col1");
        _escherClientAnchor.setCol1(col1);
    }

    public void setCol1(int col1) {
        setCol1((short) col1);
    }

    public short getCol2() {
        return _escherClientAnchor.getCol2();
    }

    public void setCol2(short col2) {
        checkRange(col2, 0, 255, "col2");
        _escherClientAnchor.setCol2(col2);
    }

    public void setCol2(int col2) {
        setCol2((short) col2);
    }

    public int getRow1() {
        return _escherClientAnchor.getRow1();
    }

    public void setRow1(int row1) {
        checkRange(row1, 0, 256 * 256, "row1");
        _escherClientAnchor.setRow1(Integer.valueOf(row1).shortValue());
    }

    public int getRow2() {
        return _escherClientAnchor.getRow2();
    }

    public void setRow2(int row2) {
        checkRange(row2, 0, 256 * 256, "row2");
        _escherClientAnchor.setRow2(Integer.valueOf(row2).shortValue());
    }

    /**
     * Dets the top-left and bottom-right
     * coordinates of the anchor.
     *
     * @param x1   the x coordinate within the first cell.
     * @param y1   the y coordinate within the first cell.
     * @param x2   the x coordinate within the second cell.
     * @param y2   the y coordinate within the second cell.
     * @param col1 the column (0 based) of the first cell.
     * @param row1 the row (0 based) of the first cell.
     * @param col2 the column (0 based) of the second cell.
     * @param row2 the row (0 based) of the second cell.
     */
    public void setAnchor(short col1, int row1, int x1, int y1, short col2, int row2, int x2, int y2) {
        checkRange(getDx1(), 0, 1023, "dx1");
        checkRange(getDx2(), 0, 1023, "dx2");
        checkRange(getDy1(), 0, 255, "dy1");
        checkRange(getDy2(), 0, 255, "dy2");
        checkRange(getCol1(), 0, 255, "col1");
        checkRange(getCol2(), 0, 255, "col2");
        checkRange(getRow1(), 0, 255 * 256, "row1");
        checkRange(getRow2(), 0, 255 * 256, "row2");

        setCol1(col1);
        setRow1(row1);
        setDx1(x1);
        setDy1(y1);
        setCol2(col2);
        setRow2(row2);
        setDx2(x2);
        setDy2(y2);
    }

    /**
     * @return true if the anchor goes from right to left.
     */
    public boolean isHorizontallyFlipped() {
        return _isHorizontallyFlipped;
    }

    /**
     * @return true if the anchor goes from bottom to top.
     */
    public boolean isVerticallyFlipped() {
        return _isVerticallyFlipped;
    }

    @Override
    public EscherRecord getEscherAnchor() {
        return _escherClientAnchor;
    }

    @Override
    protected void createEscherAnchor() {
        _escherClientAnchor = new EscherClientAnchorRecord();
    }

    /**
     * Gets the anchor type
     * <p/>
     * 0 = Move and size with Cells, 2 = Move but don't size with cells, 3 = Don't move or size with cells.
     */
    public int getAnchorType() {
        return _escherClientAnchor.getFlag();
    }

    /**
     * Sets the anchor type
     * <p/>
     * 0 = Move and size with Cells, 2 = Move but don't size with cells, 3 = Don't move or size with cells.
     */
    public void setAnchorType(int anchorType) {
//        if (0 != anchorType && 2 != anchorType && 3 != anchorType){
//            throw new IllegalArgumentException("Anchor type of shape can take only such values: 0, 2, 3");
//        }
        _escherClientAnchor.setFlag(Integer.valueOf(anchorType).shortValue());
    }

    private void checkRange(int value, int minRange, int maxRange, String varName) {
        if (value < minRange || value > maxRange)
            throw new IllegalArgumentException(varName + " must be between " + minRange + " and " + maxRange);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null)
            return false;
        if (obj == this)
            return true;
        if (obj.getClass() != getClass())
            return false;
        HSSFClientAnchor anchor = (HSSFClientAnchor) obj;

        return anchor.getCol1() == getCol1() && anchor.getCol2() == getCol2() && anchor.getDx1() == getDx1()
                && anchor.getDx2() == getDx2() && anchor.getDy1() == getDy1() && anchor.getDy2() == getDy2()
                && anchor.getRow1() == getRow1() && anchor.getRow2() == getRow2() && anchor.getAnchorType() == getAnchorType();
    }

    @Override
    public int getDx1() {
        return _escherClientAnchor.getDx1();
    }

    @Override
    public void setDx1(int dx1) {
        _escherClientAnchor.setDx1(Integer.valueOf(dx1).shortValue());
    }

    @Override
    public int getDy1() {
        return _escherClientAnchor.getDy1();
    }

    @Override
    public void setDy1(int dy1) {
        _escherClientAnchor.setDy1(Integer.valueOf(dy1).shortValue());
    }

    @Override
    public int getDy2() {
        return _escherClientAnchor.getDy2();
    }

    @Override
    public void setDy2(int dy2) {
        _escherClientAnchor.setDy2(Integer.valueOf(dy2).shortValue());
    }

    @Override
    public int getDx2() {
        return _escherClientAnchor.getDx2();
    }

    @Override
    public void setDx2(int dx2) {
        _escherClientAnchor.setDx2(Integer.valueOf(dx2).shortValue());
    }
}
