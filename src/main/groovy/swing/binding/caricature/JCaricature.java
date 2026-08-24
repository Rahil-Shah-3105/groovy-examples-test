/*
 *  Licensed to the Apache Software Foundation (ASF) under one
 *  or more contributor license agreements.  See the NOTICE file
 *  distributed with this work for additional information
 *  regarding copyright ownership.  The ASF licenses this file
 *  to you under the Apache License, Version 2.0 (the
 *  "License"); you may not use this file except in compliance
 *  with the License.  You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing,
 *  software distributed under the License is distributed on an
 *  "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 *  KIND, either express or implied.  See the License for the
 *  specific language governing permissions and limitations
 *  under the License.
 */
/*
 * Caricature.java
 *
 * Created on April 8, 2006, 4:09 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package swing.binding.caricature;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import javax.swing.ImageIcon;
import javax.swing.JPanel;

/**
 *
 * @author sky
 */
public class JCaricature extends JPanel {
	private static final Logger log = Logger.getLogger(JCaricature.class.getName());
    private Map/*<String,Image>*/ imageMap;

    private boolean empty;
    private int mouthStyle;
    private int faceStyle;
    private int hairStyle;
    private int eyeStyle;
    private int noseStyle;
    private int rotation;
    private float scale = 1.0f;

    public JCaricature() {
		log.debug(">>> Entering JCaricature()");
        if (imageMap == null) {
            imageMap = new HashMap/*<String,Image>*/(1);
            for (int i = 0; i < 5; i++) {
				log.debug("JCaricature(): imageMap → {}", imageMap);
                getImage("face", i);
                getImage("hair", i);
                getImage("eyes", i);
                getImage("nose", i);
                getImage("mouth", i);
            }
        }
		log.debug("<<< Exiting JCaricature()");
    }

    public void setEmpty(boolean empty) {
		log.debug(">>> Entering setEmpty(empty={})", empty);
        if (this.empty != empty) {
            this.empty = empty;
            firePropertyChange("empty", !empty, empty);
			log.debug("setEmpty(empty={}): this.empty → {}", empty, this.empty);
            repaint();
        }
		log.debug("<<< Exiting setEmpty(empty={})", empty);
    }

    public boolean isEmpty() {
		log.debug(">>> Entering isEmpty()");
		log.debug("<<< Exiting isEmpty()");
        return empty;
    }

    public void setRotation(int rotation) {
		log.debug(">>> Entering setRotation(rotation={})", rotation);
        int oldRotation = this.rotation;
        this.rotation = rotation;
        repaint();
		log.debug("setRotation(rotation={}): this.rotation → {}", rotation, this.rotation);
        firePropertyChange("rotation", oldRotation, rotation);
		log.debug("<<< Exiting setRotation(rotation={})", rotation);
    }

    public int getRotation() {
		log.debug(">>> Entering getRotation()");
		log.debug("<<< Exiting getRotation()");
        return rotation;
    }

    public void setScale(float scale) {
		log.debug(">>> Entering setScale(scale={})", scale);
        float oldScale = this.scale;
        this.scale = scale;
        repaint();
		log.debug("setScale(scale={}): this.scale → {}", scale, this.scale);
        firePropertyChange("scale", oldScale, scale);
		log.debug("<<< Exiting setScale(scale={})", scale);
    }

    public float getScale() {
		log.debug(">>> Entering getScale()");
		log.debug("<<< Exiting getScale()");
        return scale;
    }

    public void setMouthStyle(int style) {
		log.debug(">>> Entering setMouthStyle(style={})", style);
        int oldStyle = mouthStyle;
        mouthStyle = style;
		log.debug("setMouthStyle(style={}): mouthStyle → {}", style, mouthStyle);
        firePropertyChange("mouthStyle", oldStyle, style);
        repaint();
		log.debug("<<< Exiting setMouthStyle(style={})", style);
    }

    public int getMouthStyle() {
		log.debug(">>> Entering getMouthStyle()");
		log.debug("<<< Exiting getMouthStyle()");
        return mouthStyle;
    }

    public void setFaceStyle(int style) {
		log.debug(">>> Entering setFaceStyle(style={})", style);
        int oldStyle = faceStyle;
        faceStyle = style;
		log.debug("setFaceStyle(style={}): faceStyle → {}", style, faceStyle);
        firePropertyChange("faceStyle", oldStyle, style);
        repaint();
		log.debug("<<< Exiting setFaceStyle(style={})", style);
    }

    public int getFaceStyle() {
		log.debug(">>> Entering getFaceStyle()");
		log.debug("<<< Exiting getFaceStyle()");
        return faceStyle;
    }

    public void setHairStyle(int style) {
		log.debug(">>> Entering setHairStyle(style={})", style);
        int oldStyle = hairStyle;
        hairStyle = style;
		log.debug("setHairStyle(style={}): hairStyle → {}", style, hairStyle);
        firePropertyChange("hairStyle", oldStyle, style);
        repaint();
		log.debug("<<< Exiting setHairStyle(style={})", style);
    }

    public int getHairStyle() {
		log.debug(">>> Entering getHairStyle()");
		log.debug("<<< Exiting getHairStyle()");
        return hairStyle;
    }

    public void setEyeStyle(int style) {
		log.debug(">>> Entering setEyeStyle(style={})", style);
        int oldStyle = eyeStyle;
        eyeStyle = style;
		log.debug("setEyeStyle(style={}): eyeStyle → {}", style, eyeStyle);
        firePropertyChange("eyeStyle", oldStyle, style);
        repaint();
		log.debug("<<< Exiting setEyeStyle(style={})", style);
    }

    public int getEyeStyle() {
		log.debug(">>> Entering getEyeStyle()");
		log.debug("<<< Exiting getEyeStyle()");
        return eyeStyle;
    }

    public void setNoseStyle(int style) {
		log.debug(">>> Entering setNoseStyle(style={})", style);
        int oldStyle = noseStyle;
        noseStyle = style;
		log.debug("setNoseStyle(style={}): noseStyle → {}", style, noseStyle);
        firePropertyChange("noseStyle", oldStyle, style);
        repaint();
		log.debug("<<< Exiting setNoseStyle(style={})", style);
    }

    public int getNoseStyle() {
		log.debug(">>> Entering getNoseStyle()");
		log.debug("<<< Exiting getNoseStyle()");
        return noseStyle;
    }

    public Dimension getPreferredSize() {
		log.debug(">>> Entering getPreferredSize()");
        if (!isPreferredSizeSet()) {
            Image image = getImage("mouth", 0);
			log.debug("getPreferredSize(): image → {}", image);
		log.debug("<<< Exiting getPreferredSize()");
            return new Dimension(image.getWidth(null), image.getHeight(null));
        }
        return super.getPreferredSize();
    }

    public Dimension getMaximumSize() {
		log.debug(">>> Entering getMaximumSize()");
        if (!isMaximumSizeSet()) {
		log.debug("<<< Exiting getMaximumSize()");
            return getPreferredSize();
        }
        return super.getMaximumSize();
    }

    protected void paintComponent(Graphics g) {
		log.debug(">>> Entering paintComponent(g={})", g);
        super.paintComponent(g);
        if (empty) {
            return;
        }
        Graphics2D g2 = (Graphics2D)g.create();
		log.debug("paintComponent(g={}): g2 → {}", g, g2);
        Image image = getImage("face", getFaceStyle());
		log.debug("paintComponent(g={}): image → {}", g, image);
        int iw = image.getWidth(null);
		log.debug("paintComponent(g={}): iw → {}", g, iw);
        int ih = image.getHeight(null);
		log.debug("<<< Exiting paintComponent(g={})", g);
		log.debug("paintComponent(g={}): ih → {}", g, ih);
//        g2.translate(iw / 2, ih / 2);
        g2.translate(getWidth() / 2, getHeight() / 2);
        if (iw != getWidth()) {
            float forcedScale = (float)getWidth() / (float)iw;
			log.debug("paintComponent(g={}): forcedScale → {}", g, forcedScale);
            g2.scale(forcedScale, forcedScale);
        }
        float scale = getScale();
		log.debug("paintComponent(g={}): scale → {}", g, scale);
        if (scale != 1) {
            g2.scale((double)scale, (double)scale);
        }
        int rotation = getRotation();
		log.debug("paintComponent(g={}): rotation → {}", g, rotation);
        if (rotation != 0) {
            g2.rotate(Math.toRadians(rotation));
        }
        drawImage(g2, "face", getFaceStyle());
        drawImage(g2, "hair", getHairStyle());
        drawImage(g2, "eyes", getEyeStyle());
        drawImage(g2, "nose", getNoseStyle());
        drawImage(g2, "mouth", getMouthStyle());
        g2.dispose();
    }

    private void drawImage(Graphics g, String string, int i) {
		log.debug(">>> Entering drawImage(g={},string={},i={})", g, string, i);
        Image image = getImage(string, i);
		log.debug("drawImage(g={},string={},i={}): image → {}", g, string, i, image);
        g.drawImage(image, -image.getWidth(null) / 2, -image.getHeight(null) / 2, null);
		log.debug("<<< Exiting drawImage(g={},string={},i={})", g, string, i);
    }

    private Image getImage(String key, int style) {
		log.debug(">>> Entering getImage(key={},style={})", key, style);
        String imageName = key + (style + 1) + ".gif";
        Image image = (Image) imageMap.get(imageName);
		log.debug("getImage(key={},style={}): image → {}", key, style, image);
		log.debug("getImage(key={},style={}): imageName → {}", key, style, imageName);
        if (image == null) {
            System.err.println("name=" + imageName);
            URL imageLoc = getClass().getResource("resources/" + imageName);
			log.debug("getImage(key={},style={}): imageLoc → {}", key, style, imageLoc);
            image = new ImageIcon(imageLoc).getImage();
            imageMap.put(imageName, image);
        }
		log.debug("<<< Exiting getImage(key={},style={})", key, style);
        return image;
    }
}
