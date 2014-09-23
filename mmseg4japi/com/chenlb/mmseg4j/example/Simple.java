package com.chenlb.mmseg4j.example;

import java.io.IOException;

import com.chenlb.mmseg4j.Seg;
import com.chenlb.mmseg4j.SimpleSeg;

/**
 * 
 * @author chenlb 2009-3-14 上午12:38:40
 */
public class Simple extends Complex {
	
	protected Seg getSeg() {

		return new SimpleSeg(dic);
	}
	
	public String[] cutWord(String text) {
		if(null==text)
		{
			return null;
		}
		try {
			return cut(text);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}

	public static void main(String[] args) throws IOException {
		String[] rStrings = new Simple().cutWord("中科鼎富分词测试，新浪表情来一个[微笑]，腾讯表情/生气");
		for (int i = 0; i < rStrings.length; i++) {
			System.out.println(rStrings[i]);
		}
	}

}
