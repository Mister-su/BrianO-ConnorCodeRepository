package com.chenlb.mmseg4j.example;

import java.io.IOException;

import com.chenlb.mmseg4j.MaxWordSeg;
import com.chenlb.mmseg4j.Seg;

public class MaxWord extends Complex {

	protected Seg getSeg() {

		return new MaxWordSeg(dic);
	}
	public String[] cutWord(String text) {
        try {
            return cut(text);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return null;
        }
    }
	public static void main(String[] args) throws IOException {
//		new MaxWord().run(args);
	    String[] rStrings = new MaxWord().cutWord("中科鼎富分词测试阳光明媚，新浪表情来一个[微笑]，腾讯表情/生气");
        for (int i = 0; i < rStrings.length; i++) {
            System.out.println(rStrings[i]);
        }
	}
}
