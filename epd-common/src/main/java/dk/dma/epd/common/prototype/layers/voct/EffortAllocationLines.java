/* Copyright (c) 2011 Danish Maritime Authority.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package dk.dma.epd.common.prototype.layers.voct;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

import com.bbn.openmap.omGraphics.OMGraphicList;
import com.bbn.openmap.omGraphics.OMLine;

import dk.dma.enav.model.geometry.Position;
import dk.dma.epd.common.prototype.layers.voct.EffortAllocationAreaGraphics.LineType;

public class EffortAllocationLines extends OMGraphicList{

    private static final long serialVersionUID = 1L;
    float[] dash = { 0.1f };
    LineType type;
    Position A;
    Position B;
    
    EffortAllocationAreaGraphics effectiveSRUAreaGraphics;
    
    public EffortAllocationLines(Position pointA, Position pointB, LineType type, EffortAllocationAreaGraphics effectiveSRUAreaGraphics){
        this.type = type;
        this.setVague(true);
        this.effectiveSRUAreaGraphics = effectiveSRUAreaGraphics;
        
        this.A = pointA;
        this.B = pointB;
        
        
        
        lineType = LINETYPE_RHUMB;
        drawLine(pointA, pointB);
    }
 
    
    
    /**
     * @return the a
     */
    public Position getA() {
        return A;
    }



    /**
     * @return the b
     */
    public Position getB() {
        return B;
    }



    /**
     * @return the type
     */
    public LineType getType() {
        return type;
    }



    private void drawLine(Position A, Position B){
        this.A = A;
        this.B = B;
        
        OMLine line = new OMLine(A.getLatitude(), A.getLongitude(), B.getLatitude(), B.getLongitude(), lineType);
        
        line.setLinePaint(Color.black);
        line.setStroke(new BasicStroke(2.0f, BasicStroke.JOIN_MITER,
                BasicStroke.JOIN_MITER, 1.0f, dash, 0.0f));

        
        add(line);

        
    }
    
    public void updateLine(Position A, Position B){
        this.clear();
        drawLine(A, B);
    }
    
    public void updateArea(Position pos){
        //The position is the new one
        
        //we need to change both lat and lon
        
        
        effectiveSRUAreaGraphics.updateLength(type, pos);
        
//        //change lat
//        if (this.type == LineType.BOTTOM || this.type == LineType.TOP){
//            Position newPos = Position.create(pos.getLatitude(), A.getLongitude());
//            
//            effectiveSRUAreaGraphics.updateLength(type, newPos);
//            
//    
//        }else{
//            // change lon
//            if (this.type == LineType.LEFT || this.type == LineType.RIGHT){
//                Position newPos = Position.create(A.getLatitude(), pos.getLongitude());
//                
//                effectiveSRUAreaGraphics.updateLength(type, newPos);
//            }
//        }
        
        
        
    }
    
    @Override
    public void render(Graphics gr) {
        Graphics2D image = (Graphics2D) gr;
        image.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);
        super.render(image);
    }
}
