package com.example.testpic.model;

import java.io.Serializable;

/**
 *
 * 
 * @author Administrator
 * 
 */
public class ImageItem implements Serializable {
	public String imageId;
	public String thumbnailPath;
	public String imagePath;
	public boolean isSelected = false;
}
