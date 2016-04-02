package com.cwp.cmoneycharge.utils;

import android.app.Activity;
import android.content.Context;
import android.view.View;

import com.cwp.cmoneycharge.Effectstype;
import com.cwp.cmoneycharge.R;
import com.cwp.cmoneycharge.activity.AddPay;
import com.cwp.cmoneycharge.app.SysApplication;
import com.cwp.cmoneycharge.widget.NiftyDialogBuilder;

public class DialogShowUtil {
	private Context ctx;
	private Activity act;
	NiftyDialogBuilder dialogBuilder = null;
	private Effectstype effect; // �Զ���Dialog
	String[] VoiceSave;
	static String type;
	static String VoiceDefault;

	public DialogShowUtil(Activity act, Context ctx, String[] VoiceSave,
			String type, String VoiceDefault) {
		this.ctx = ctx;
		this.act = act;
		this.VoiceSave = VoiceSave;
		this.type = type;
		this.VoiceDefault = VoiceDefault;
	}

	public void dialogShow(String showtype, String style,
			final String context1, String context2) {
		dialogBuilder = new NiftyDialogBuilder(ctx, R.style.dialog_untran); // �Զ���dialogBuilder
		switch (showtype) {
		case "rotatebottom":
			effect = Effectstype.RotateBottom;
			break;
		case "shake":
			effect = Effectstype.Shake;
			break;
		}

		switch (style) {
		case "first":
			dialogBuilder.withTitle("��������")
					// .withTitle(null) no title
					.withTitleColor("#FFFFFF")
					// def
					.withDividerColor("#11000000")
					// def
					.withMessage("������ʽ��\n����ڲ���ʳ��20Ԫ��\n\n")
					// .withMessage(null) no Msg
					.withMessageColor("#FFFFFF")
					// def
					.withIcon(ctx.getResources().getDrawable(R.drawable.icon))
					.isCancelableOnTouchOutside(false) // def |//
														// isCancelable(true)
					.withDuration(700) // def
					.withEffect(effect) // def Effectstype.Slidetop
					.withButton1Text("ȡ��") // def gone
					.withButton2Text("��ʼ����") // def gone
					.setButton1Click(new View.OnClickListener() {
						@Override
						public void onClick(View v) {
							dialogBuilder.dismiss();
						}
					}).setButton2Click(new View.OnClickListener() {
						@Override
						public void onClick(View v) {
							dialogBuilder.dismiss();
							((AddPay) act).VoiceRecognition();
						}
					}).show();
			break;
		case "notype":
			dialogBuilder.withTitle("ʶ��ɹ�")
					// .withTitle(null) no title
					.withTitleColor("#FFFFFF")
					// def
					.withDividerColor("#11000000")
					// def
					.withMessage("��ո�˵�ˡ� " + context1 + "��\n\n" + context2)
					// .withMessage(null) no Msg
					.withMessageColor("#FFFFFF")
					// def
					.withIcon(ctx.getResources().getDrawable(R.drawable.icon))
					.isCancelableOnTouchOutside(false) // def |//
					.withDuration(700) // def
					.withEffect(effect) // def Effectstype.Slidetop
					.withButton1Text("ȡ��") // def gone
					.withButton2Text("��") // def gone
					.setButton1Click(new View.OnClickListener() {
						@Override
						public void onClick(View v) {
							dialogBuilder.dismiss();
						}
					}).setButton2Click(new View.OnClickListener() {
						@Override
						public void onClick(View v) {
							dialogBuilder.dismiss();
							VoiceDefault = "notype";
							VoiceSave[3] = VoiceSave[3];
							dialogShow("shake", "judge", context1, "");
						}
					}).show();
			break;
		case "wrong":
			dialogBuilder
					.withTitle("ʶ��ʧ��")
					// .withTitle(null) no title
					.withTitleColor("#FFFFFF")
					// def
					.withDividerColor("#11000000")
					// def
					.withMessage(
							"��ո�˵�ˡ� " + context1 + "������ϸ�ʽ��������һ��\n\n"
									+ context2)
					// .withMessage(null) no Msg
					.withMessageColor("#FFFFFF")
					// def
					.withIcon(ctx.getResources().getDrawable(R.drawable.icon))
					.isCancelableOnTouchOutside(false) // def |
														// isCancelable(true)
					.withDuration(700) // def
					.withEffect(effect) // def Effectstype.Slidetop
					.withButton1Text("ȡ��") // def gone
					.withButton2Text("�ٴ�����") // def gone
					.setButton1Click(new View.OnClickListener() {
						@Override
						public void onClick(View v) {
							dialogBuilder.dismiss();
						}
					}).setButton2Click(new View.OnClickListener() {
						@Override
						public void onClick(View v) {
							dialogBuilder.dismiss();
							((AddPay) act).VoiceRecognition();
						}
					}).show();
			break;
		case "OK":
			dialogBuilder.withTitle("ʶ��ɹ�")
					// .withTitle(null) no title
					.withTitleColor("#FFFFFF")
					// def
					.withDividerColor("#11000000")
					// def
					.withMessage("�ɹ���\n��ո�˵�ˡ�" + context1 + "����\n�Ƿ�ȷ��Ҫ��¼�������?")
					// .withMessage(null) no Msg
					.withMessageColor("#FFFFFF")
					// def
					.withIcon(ctx.getResources().getDrawable(R.drawable.icon))
					.isCancelableOnTouchOutside(false) // def |
														// isCancelable(true)
					.withDuration(700) // def
					.withEffect(effect) // def Effectstype.Slidetop
					.withButton1Text("ȡ��") // def gone
					.withButton2Text("ȷ��") // def gone
					.setButton1Click(new View.OnClickListener() {
						@Override
						public void onClick(View v) {
							dialogBuilder.dismiss();
						}
					}).setButton2Click(new View.OnClickListener() {
						@Override
						public void onClick(View v) {
							dialogBuilder.dismiss();
							((AddPay) act).VoiceSuccess();
						}
					}).show();
			break;
		case "judge":
			dialogBuilder
					.withTitle("ʶ��ɹ�")
					// .withTitle(null) no title
					.withTitleColor("#FFFFFF")
					// def
					.withDividerColor("#11000000")
					// def
					.withMessage(
							"�ɹ���\n��ո�˵�ˡ�" + context1 + "����\n<" + VoiceSave[3]
									+ ">�����Ҫ����ȷ�ϸñ���<��֧>����<����>?\n")
					// .withMessage(null) no Msg
					.withMessageColor("#FFFFFF")
					// def
					.withIcon(ctx.getResources().getDrawable(R.drawable.icon))
					.isCancelableOnTouchOutside(false) // def |
														// isCancelable(true)
					.withDuration(700) // def
					.withEffect(effect) // def Effectstype.Slidetop
					.withButton1Text("��֧") // def gone
					.withButton2Text("����") // def gone
					.setButton1Click(new View.OnClickListener() {
						@Override
						public void onClick(View v) {
							dialogBuilder.dismiss();
							type = "pay";
							((AddPay) act).VoiceSuccess();
						}
					}).setButton2Click(new View.OnClickListener() {
						@Override
						public void onClick(View v) {
							dialogBuilder.dismiss();
							type = "income";
							((AddPay) act).VoiceSuccess();
						}
					}).show();
			break;
		case "quit":
			dialogBuilder.withTitle("�˳�����")
					// .withTitle(null) no title
					.withTitleColor("#FFFFFF")
					// def
					.withDividerColor("#11000000")
					// def
					.withMessage("�Ƿ�Ҫ�˳�����\n\n")
					// .withMessage(null) no Msg
					.withMessageColor("#FFFFFF")
					// def
					.withIcon(ctx.getResources().getDrawable(R.drawable.icon))
					.isCancelableOnTouchOutside(false) // def |
														// isCancelable(true)
					.withDuration(700) // def
					.withEffect(effect) // def Effectstype.Slidetop
					.withButton1Text("ȡ��") // def gone
					.withButton2Text("�˳�") // def gone
					.setButton1Click(new View.OnClickListener() {
						@Override
						public void onClick(View v) {
							dialogBuilder.dismiss();
						}
					}).setButton2Click(new View.OnClickListener() {
						@Override
						public void onClick(View v) {
							SysApplication.getInstance().exit();
						}
					}).show();
			break;
		}

	}

	public static String dialoggettype() {
		return type;
	}

	public static String dialogVoiceDefault() {
		return VoiceDefault;
	}
}