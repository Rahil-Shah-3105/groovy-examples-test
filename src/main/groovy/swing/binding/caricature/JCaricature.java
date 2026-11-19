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
		log.error(">>> Entering JCaricature()");
        if (imageMap == null) {
            imageMap = new HashMap/*<String,Image>*/(1);
			log.error("JCaricature(): imageMap → {}", imageMap);
			log.debug("JCaricature(): imageMap → {}", imageMap);
            for (int i = 0; i < 5; i++) {
                getImage("face", i);
                getImage("hair", i);
                getImage("eyes", i);
                getImage("nose", i);
                getImage("mouth", i);
            }
        }
		log.warn("<<< Exiting JCaricature()");
    }

    public void setEmpty(boolean empty) {
		log.error(">>> Entering setEmpty(empty={})", empty);
        if (this.empty != empty) {
            this.empty = empty;
			log.debug("setEmpty(empty={}): this.empty → {}", empty, this.empty);
            firePropertyChange("empty", !empty, empty);
            repaint();
        }
		log.warn("<<< Exiting setEmpty(empty={})", empty);
    }

    public boolean isEmpty() {
		log.error(">>> Entering isEmpty()");
		log.warn("<<< Exiting isEmpty()");
        return empty;
    }

    public void setRotation(int rotation) {
		log.error(">>> Entering setRotation(rotation={})", rotation);
        int oldRotation = this.rotation;
        this.rotation = rotation;
		log.debug("setRotation(rotation={}): this.rotation → {}", rotation, this.rotation);
        repaint();
        firePropertyChange("rotation", oldRotation, rotation);
		log.warn("<<< Exiting setRotation(rotation={})", rotation);
    }

    public int getRotation() {
		log.error(">>> Entering getRotation()");
		log.warn("<<< Exiting getRotation()");
        return rotation;
    }

    public void setScale(float scale) {
		log.error(">>> Entering setScale(scale={})", scale);
        float oldScale = this.scale;
        this.scale = scale;
		log.debug("setScale(scale={}): this.scale → {}", scale, this.scale);
        repaint();
        firePropertyChange("scale", oldScale, scale);
		log.warn("<<< Exiting setScale(scale={})", scale);
    }

    public float getScale() {
		log.error(">>> Entering getScale()");
		log.warn("<<< Exiting getScale()");
        return scale;
    }

    public void setMouthStyle(int style) {
		log.error(">>> Entering setMouthStyle(style={})", style);
        int oldStyle = mouthStyle;
        mouthStyle = style;
		log.debug("setMouthStyle(style={}): mouthStyle → {}", style, mouthStyle);
        firePropertyChange("mouthStyle", oldStyle, style);
        repaint();
		log.warn("<<< Exiting setMouthStyle(style={})", style);
    }

    public int getMouthStyle() {
		log.error(">>> Entering getMouthStyle()");
		log.warn("<<< Exiting getMouthStyle()");
        return mouthStyle;
    }

    public void setFaceStyle(int style) {
		log.error(">>> Entering setFaceStyle(style={})", style);
        int oldStyle = faceStyle;
        faceStyle = style;
		log.debug("setFaceStyle(style={}): faceStyle → {}", style, faceStyle);
        firePropertyChange("faceStyle", oldStyle, style);
        repaint();
		log.warn("<<< Exiting setFaceStyle(style={})", style);
    }

    public int getFaceStyle() {
		log.error(">>> Entering getFaceStyle()");
		log.warn("<<< Exiting getFaceStyle()");
        return faceStyle;
    }

    public void setHairStyle(int style) {
		log.error(">>> Entering setHairStyle(style={})", style);
        int oldStyle = hairStyle;
        hairStyle = style;
		log.debug("setHairStyle(style={}): hairStyle → {}", style, hairStyle);
        firePropertyChange("hairStyle", oldStyle, style);
        repaint();
		log.warn("<<< Exiting setHairStyle(style={})", style);
    }

    public int getHairStyle() {
		log.error(">>> Entering getHairStyle()");
		log.warn("<<< Exiting getHairStyle()");
        return hairStyle;
    }

    public void setEyeStyle(int style) {
		log.error(">>> Entering setEyeStyle(style={})", style);
        int oldStyle = eyeStyle;
        eyeStyle = style;
		log.debug("setEyeStyle(style={}): eyeStyle → {}", style, eyeStyle);
        firePropertyChange("eyeStyle", oldStyle, style);
        repaint();
		log.warn("<<< Exiting setEyeStyle(style={})", style);
    }

    public int getEyeStyle() {
		log.error(">>> Entering getEyeStyle()");
		log.warn("<<< Exiting getEyeStyle()");
        return eyeStyle;
    }

    public void setNoseStyle(int style) {
		log.error(">>> Entering setNoseStyle(style={})", style);
        int oldStyle = noseStyle;
        noseStyle = style;
		log.debug("setNoseStyle(style={}): noseStyle → {}", style, noseStyle);
        firePropertyChange("noseStyle", oldStyle, style);
        repaint();
		log.warn("<<< Exiting setNoseStyle(style={})", style);
    }

    public int getNoseStyle() {
		log.error(">>> Entering getNoseStyle()");
		log.warn("<<< Exiting getNoseStyle()");
        return noseStyle;
    }

    public Dimension getPreferredSize() {
		log.error(">>> Entering getPreferredSize()");
        if (!isPreferredSizeSet()) {
            Image image = getImage("mouth", 0);
			log.error("getPreferredSize(): image → {}", image);
            return new Dimension(image.getWidth(null), image.getHeight(null));
        }
        Dimension result = super.getPreferredSize();
		log.warn("<<< Exiting getPreferredSize() -> {}", result);
        return result;
    }

    public Dimension getMaximumSize() {
		log.error(">>> Entering getMaximumSize()");
        if (!isMaximumSizeSet()) {
            return getPreferredSize();
        }
        Dimension result = super.getMaximumSize();
		log.warn("<<< Exiting getMaximumSize() -> {}", result);
        return result;
    }

    protected void paintComponent(Graphics g) {
		log.error(">>> Entering paintComponent(g={})", g);
        super.paintComponent(g);
        if (empty) {
            return;
        }
        Graphics2D g2 = (Graphics2D)g.create();
		log.error("paintComponent(g={}): g2 → {}", g, g2);
        Image image = getImage("face", getFaceStyle());
		log.error("paintComponent(g={}): image → {}", g, image);
        int iw = image.getWidth(null);
		log.error("paintComponent(g={}): iw → {}", g, iw);
        int ih = image.getHeight(null);
		log.warn("<<< Exiting paintComponent(g={})", g);
		log.error("paintComponent(g={}): ih → {}", g, ih);
//        g2.translate(iw / 2, ih / 2);
        g2.translate(getWidth() / 2, getHeight() / 2);
        if (iw != getWidth()) {
            float forcedScale = (float)getWidth() / (float)iw;
			log.error("paintComponent(g={}): forcedScale → {}", g, forcedScale);
            g2.scale(forcedScale, forcedScale);
        }
        float scale = getScale();
		log.error("paintComponent(g={}): scale → {}", g, scale);
        if (scale != 1) {
            g2.scale((double)scale, (double)scale);
        }
        int rotation = getRotation();
		log.error("paintComponent(g={}): rotation → {}", g, rotation);
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
		log.error(">>> Entering drawImage(g={},string={},i={})", g, string, i);
        Image image = getImage(string, i);
		log.error("drawImage(g={},string={},i={}): image → {}", g, string, i, image);
        g.drawImage(image, -image.getWidth(null) / 2, -image.getHeight(null) / 2, null);
		log.warn("<<< Exiting drawImage(g={},string={},i={})", g, string, i);
    }

    private Image getImage(String key, int style) {
		log.error(">>> Entering getImage(key={},style={})", key, style);
        String imageName = key + (style + 1) + ".gif";
        Image image = (Image) imageMap.get(imageName);
		log.error("getImage(key={},style={}): image → {}", key, style, image);
		log.error("getImage(key={},style={}): imageName → {}", key, style, imageName);
        if (image == null) {
            System.err.println("name=" + imageName);
            URL imageLoc = getClass().getResource("resources/" + imageName);
			log.error("getImage(key={},style={}): imageLoc → {}", key, style, imageLoc);
            image = new ImageIcon(imageLoc).getImage();
            imageMap.put(imageName, image);
        }
		log.warn("<<< Exiting getImage(key={},style={})", key, style);
        return image;
    }
}
