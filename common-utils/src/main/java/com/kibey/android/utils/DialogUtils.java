package com.kibey.android.utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;

import com.kibey.common_utils.R;

import cn.pedant.SweetAlert.IDialogDismiss;
import cn.pedant.SweetAlert.SweetAlertDialog;

/**
 * Created by mchwind on 15/8/8.
 */
public class DialogUtils {
    private static Context mContext = AppProxy.getApp();
    private static boolean isDialogFragmentStyle;
    AlertDialog dialog;

    public static void setIsDialogFragmentStyle(boolean isDialogFragmentStyle) {
        DialogUtils.isDialogFragmentStyle = isDialogFragmentStyle;
    }


    public DialogUtils(FragmentActivity activity) {
        dialog = new ProgressDialog(activity);
    }

    public void dismiss() {
        if (dialog != null) {
            dialog.dismiss();
        }
    }

    public void setCancelable(boolean progressBarCancelable) {
        if (dialog != null) {
            dialog.setCancelable(progressBarCancelable);
        }
    }


    public void setMessage(String msg) {
        if (dialog != null) {
            dialog.setMessage(msg);
        }
    }

    public void show() {
        if (dialog != null) {
            try {
                dialog.show();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static IDialogDismiss showError(int errorId) {
        return show(errorId, SweetAlertDialog.ERROR_TYPE);
    }

    public static IDialogDismiss showError(String errorMsg) {
        return show(errorMsg, SweetAlertDialog.ERROR_TYPE);
    }


    public static IDialogDismiss show(int stringId, int type) {
        return show(stringId, type, null);
    }

    public static IDialogDismiss show(String stringId, int type) {
        return show(stringId, type, null);
    }

    public static IDialogDismiss show(int resId, int type, OnDismissListener listener) {
        String content = getApp().getString(resId);
        return show(content, type, listener);
    }

    private static Context getApp() {
        return AppProxy.getApp();
    }

    public static IDialogDismiss show(String content, int type, OnDismissListener listener) {
        return show(content, type, listener, true);
    }

    public static IDialogDismiss show(String content, int type, OnDismissListener listener, boolean dialogFragment) {
        Activity activity = APPConfig.getFirstActivity();
        return show(activity, content, type, listener, dialogFragment);
    }

    public static IDialogDismiss show(Activity activity, String content, int type, DialogInterface.OnDismissListener listener, boolean dialogFragment) {
        if (null != activity) {
            String title = mContext.getString(R.string.tip);
            if (dialogFragment && isDialogFragmentStyle() && activity instanceof FragmentActivity) {
                return ActionDialogFragment.show((FragmentActivity) activity, type, title, content, listener);
            } else {
                SweetAlertDialog dialog = getSweetDialog(activity, type, title, content);
                dialog.setOnDismissListener(listener);
                dialog.show();
                return dialog;
            }

        } else {
            Utils.toast(mContext, content);
        }
        return null;
    }

    private static boolean isDialogFragmentStyle() {
        return isDialogFragmentStyle;
    }

    private static SweetAlertDialog getSweetDialog(Activity activity, int type, String title, String content) {
        SweetAlertDialog dialog = new SweetAlertDialog(activity, type);
        dialog.setTitleText(title);
        dialog.setContentText(content);
        return dialog;
    }

    public static class ActionDialogFragment extends DialogFragment implements IDialogDismiss {

        private static final String EXTRA_TYPE = "EXTRA_TYPE";
        private static final String EXTRA_TITLE = "EXTRA_TITLE";
        private static final String EXTRA_CONTENT = "EXTRA_CONTENT";

        public static ActionDialogFragment show(FragmentActivity activity, int type, String title, String content, DialogInterface.OnDismissListener onDismissListener) {
            ActionDialogFragment fragment = new ActionDialogFragment();
            Bundle bundle = new Bundle();
            bundle.putInt(EXTRA_TYPE, type);
            bundle.putString(EXTRA_TITLE, title);
            bundle.putString(EXTRA_CONTENT, content);
            fragment.setArguments(bundle);
            fragment.onDismissListener = onDismissListener;

            fragment.show(activity.getSupportFragmentManager(), "ActionDialog");
            return fragment;
        }

        private int type;

        SweetAlertDialog dialog;

        DialogInterface.OnDismissListener onDismissListener;

        private String title;

        private String content;

        @Override
        public void show(FragmentManager manager, String tag) {
//        super.show(manager, tag);
            // 用commitAllowingStateLoss防止崩溃
            manager.beginTransaction().add(this, tag).commitAllowingStateLoss();
        }

        @NonNull
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            Bundle args = getArguments();
            if (null != args) {
                type = args.getInt(EXTRA_TYPE);
                title = args.getString(EXTRA_TITLE);
                content = args.getString(EXTRA_CONTENT);
            }

            SweetAlertDialog dialog = getSweetDialog(getActivity(), type, title, content);
            dialog.setOnDismissListener(onDismissListener);

            dialog.setConfirmText(R.string.ok);

            return dialog;
        }

        public void setContent(String content) {
            this.content = content;
            if (null != dialog) {
                dialog.setContentText(content);
            }
        }

        public void setTitle(String title) {
            this.title = title;
            if (null != dialog) {
                dialog.setTitleText(content);
            }
        }

        public void setOnDismissListener(DialogInterface.OnDismissListener onDismissListener) {
            this.onDismissListener = onDismissListener;
            if (null != dialog) {
                dialog.setOnDismissListener(onDismissListener);
            }
        }

        @Override
        public void onDismiss(DialogInterface dialog) {
            super.onDismiss(dialog);
            if (onDismissListener != null) {
                onDismissListener.onDismiss(dialog);
            }
        }

        @Override
        public void onDestroy() {
            super.onDestroy();
            dialog = null;
        }
    }

}
