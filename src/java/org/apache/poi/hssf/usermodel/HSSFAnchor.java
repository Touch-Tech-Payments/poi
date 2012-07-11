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


import org.apache.poi.ddf.EscherChildAnchorRecord;
import org.apache.poi.ddf.EscherClientAnchorRecord;
import org.apache.poi.ddf.EscherContainerRecord;
import org.apache.poi.ddf.EscherRecord;

/**
 * An anchor is what specifics the position of a shape within a client object
 * or within another containing shape.
 *
 * @author Glen Stampoultzis (glens at apache.org)
 */
public abstract class HSSFAnchor {

    protected boolean _isHorizontallyFlipped = false;
    protected boolean _isVerticallyFlipped = false;

    public HSSFAnchor() {
        createEscherAnchor();
    }

    public HSSFAnchor(int dx1, int dy1, int dx2, int dy2) {
        createEscherAnchor();
        setDx1(dx1);
        setDy1(dy1);
        setDx2(dx2);
        setDy2(dy2);
    }

    public static HSSFAnchor createAnchorFromEscher(EscherContainerRecord container){
        if (null != container.getChildById(EscherChildAnchorRecord.RECORD_ID)){
            return new HSSFChildAnchor((EscherChildAnchorRecord) container.getChildById(EscherChildAnchorRecord.RECORD_ID));
        } else {
            if (null != container.getChildById(EscherClientAnchorRecord.RECORD_ID)){
                return new HSSFClientAnchor((EscherClientAnchorRecord) container.getChildById(EscherClientAnchorRecord.RECORD_ID));
            }
            return null;
        }
    }

    public abstract int getDx1();

    public abstract void setDx1(int dx1);

    public abstract int getDy1();

    public abstract void setDy1(int dy1);

    public abstract int getDy2();

    public abstract void setDy2(int dy2);

    public abstract int getDx2();

    public abstract void setDx2(int dx2);

    public abstract boolean isHorizontallyFlipped();

    public abstract boolean isVerticallyFlipped();

    public abstract EscherRecord getEscherAnchor();

    protected abstract void createEscherAnchor();
}
