package com.utility.view;

public class GetFontFace {

	public static String getFontFace(int font_type) {
		String fontType = "";
		switch (font_type) {
		case 0:
			fontType = "DROIDSANS_BOLD.TTF";
			break;
		case 1:
			fontType = "DROIDSANS.TTF";
			break;
		case 2:
			fontType = "HELVETICA_NEUE_BOLD_ITALIC.TTF";
			break;
		case 3:
			fontType = "HELVETICA_NEUE_BOLD.TTF";
			break;
		case 4:
			fontType = "HELVETICA_NEUE.TTF";
			break;
		case 5:
			fontType = "UBUNTU_REGULAR.TTF";
			break;
		case 6:
			fontType = "UBUNTU_REGULAR_BOLD.TTF";
			break;
		case 7:
			fontType = "GOTHIC_0.TTF";
			break;
		case 8:
			fontType = "GOTHICB_0.TTF";
			break;
		case 9:
			fontType = "GOTHICBI_0.TTF";
			break;
		case 10:
			fontType = "GOTHICI_0.TTF";
			break;

		default:
			break;
		}
		return fontType;
	}

}
