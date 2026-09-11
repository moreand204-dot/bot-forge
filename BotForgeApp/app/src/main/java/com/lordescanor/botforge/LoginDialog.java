package com.lordescanor.botforge;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.text.InputType;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class LoginDialog extends Dialog {
    private final Context context;
    private EditText username;
    private EditText password;
    private TextView error;
    private boolean passwordVisible = false;
    private static final String FIXED_USERNAME = "Lord ESCANOR";
    private static final String FIXED_PASSWORD = "12345678";

    public LoginDialog(Context context) {
        super(context);
        this.context = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(buildContent());
        Window w = getWindow();
        if (w != null) {
            w.setBackgroundDrawableResource(android.R.color.transparent);
            w.setDimAmount(0.82f);
            w.addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
            WindowManager.LayoutParams p = w.getAttributes();
            p.width = WindowManager.LayoutParams.MATCH_PARENT;
            p.height = WindowManager.LayoutParams.WRAP_CONTENT;
            p.gravity = Gravity.CENTER;
            p.dimAmount = 0.82f;
            w.setAttributes(p);
        }
        setCanceledOnTouchOutside(false);
        setCancelable(false);
    }

    private View buildContent() {
        LinearLayout card = new LinearLayout(context);
        card.setOrientation(LinearLayout.VERTICAL);
        card.setGravity(Gravity.CENTER_HORIZONTAL);
        card.setPadding(dp(22), dp(18), dp(22), dp(22));
        card.setBackground(panel());

        TextView close = text("×", 32, Color.WHITE);
        close.setGravity(Gravity.CENTER);
        GradientDrawable closeBg = circle("#241014", "#D7193F", 2);
        close.setBackground(closeBg);
        LinearLayout.LayoutParams cp = new LinearLayout.LayoutParams(dp(46), dp(46));
        cp.gravity = Gravity.END;
        close.setLayoutParams(cp);
        close.setOnClickListener(v -> Toast.makeText(context, "يجب تسجيل الدخول للمتابعة", Toast.LENGTH_SHORT).show());
        card.addView(close);

        TextView crown = text("♛", 48, Color.WHITE);
        crown.setGravity(Gravity.CENTER);
        crown.setBackground(circle("#22070B", "#FF243D", 2));
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(dp(82), dp(82));
        lp.gravity = Gravity.CENTER_HORIZONTAL;
        lp.topMargin = -dp(14);
        card.addView(crown, lp);

        TextView title = text("LORD ESCANOR", 25, Color.WHITE);
        title.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
        title.setGravity(Gravity.CENTER);
        card.addView(title, margin(0, 12, 0, 0));

        TextView subtitle = text("AUTHORIZED ACCESS", 11, Color.rgb(235, 55, 72));
        subtitle.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
        subtitle.setGravity(Gravity.CENTER);
        card.addView(subtitle, margin(0, 2, 0, 10));

        TextView ar = text("يرجى تسجيل الدخول للمتابعة", 17, Color.WHITE);
        ar.setGravity(Gravity.CENTER);
        card.addView(ar, margin(0, 2, 0, 18));

        username = field("اسم المستخدم", false);
        card.addView(username, margin(0, 0, 0, 12));

        LinearLayout passRow = new LinearLayout(context);
        passRow.setGravity(Gravity.CENTER_VERTICAL);
        passRow.setPadding(0, 0, 0, 0);
        GradientDrawable passBg = fieldBg();
        passRow.setBackground(passBg);

        password = field("كلمة المرور", true);
        passRow.addView(password, new LinearLayout.LayoutParams(0, dp(58), 1));

        TextView eye = text("◉", 22, Color.LTGRAY);
        eye.setGravity(Gravity.CENTER);
        passRow.addView(eye, new LinearLayout.LayoutParams(dp(52), dp(58)));
        eye.setOnClickListener(v -> {
            passwordVisible = !passwordVisible;
            password.setInputType(InputType.TYPE_CLASS_TEXT | (passwordVisible ? InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD : InputType.TYPE_TEXT_VARIATION_PASSWORD));
            password.setSelection(password.length());
            eye.setText(passwordVisible ? "◉" : "○");
        });
        card.addView(passRow, margin(0, 0, 0, 8));

        error = text("", 13, Color.rgb(255, 90, 105));
        error.setGravity(Gravity.CENTER);
        error.setVisibility(View.GONE);
        card.addView(error, margin(0, 0, 0, 4));

        TextView login = text("تسجيل الدخول  »", 18, Color.WHITE);
        login.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
        login.setGravity(Gravity.CENTER);
        login.setBackground(buttonBg());
        card.addView(login, margin(0, 8, 0, 12));
        login.setOnClickListener(v -> validate());

        TextView secure = text("🛡  دخول محلي محمي  •  LORD ESCANOR", 12, Color.rgb(160, 160, 170));
        secure.setGravity(Gravity.CENTER);
        card.addView(secure);

        return card;
    }

    private void validate() {
        String u = username.getText().toString().trim();
        String p = password.getText().toString();
        if (u.isEmpty()) {
            showError("اكتب اسم المستخدم");
            username.requestFocus();
            return;
        }
        if (p.isEmpty()) {
            showError("اكتب كلمة المرور");
            password.requestFocus();
            return;
        }
        if (FIXED_USERNAME.equals(u) && FIXED_PASSWORD.equals(p)) {
            SharedPreferences sp = context.getSharedPreferences("botforge_auth", Context.MODE_PRIVATE);
            sp.edit().putBoolean("logged_in", true).apply();
            dismiss();
            if (context instanceof SplashActivity) {
                ((SplashActivity) context).openDashboard();
            }
        } else {
            showError("اسم المستخدم أو كلمة المرور غير صحيحة");
            password.setText("");
        }
    }

    private void showError(String s) {
        error.setText(s);
        error.setVisibility(View.VISIBLE);
    }

    private EditText field(String hint, boolean pass) {
        EditText e = new EditText(context);
        e.setHint(hint);
        e.setHintTextColor(Color.rgb(135, 135, 145));
        e.setTextColor(Color.WHITE);
        e.setTextSize(15);
        e.setSingleLine(true);
        e.setGravity(Gravity.RIGHT | Gravity.CENTER_VERTICAL);
        e.setPadding(dp(18), 0, dp(18), 0);
        e.setBackground(fieldBg());
        if (pass) e.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        else e.setInputType(InputType.TYPE_CLASS_TEXT);
        return e;
    }

    private GradientDrawable panel() {
        GradientDrawable g = new GradientDrawable();
        g.setColor(Color.rgb(12, 8, 10));
        g.setCornerRadius(dp(26));
        g.setStroke(dp(2), Color.rgb(115, 18, 30));
        return g;
    }

    private GradientDrawable fieldBg() {
        GradientDrawable g = new GradientDrawable();
        g.setColor(Color.rgb(18, 14, 16));
        g.setCornerRadius(dp(16));
        g.setStroke(dp(1), Color.rgb(115, 35, 45));
        return g;
    }

    private GradientDrawable buttonBg() {
        GradientDrawable g = new GradientDrawable();
        g.setColor(Color.rgb(205, 20, 45));
        g.setCornerRadius(dp(18));
        g.setStroke(dp(2), Color.rgb(255, 92, 106));
        return g;
    }

    private GradientDrawable circle(String fill, String stroke, int width) {
        GradientDrawable g = new GradientDrawable();
        g.setShape(GradientDrawable.OVAL);
        g.setColor(Color.parseColor(fill));
        g.setStroke(dp(width), Color.parseColor(stroke));
        return g;
    }

    private TextView text(String s, int size, int color) {
        TextView t = new TextView(context);
        t.setText(s);
        t.setTextSize(size);
        t.setTextColor(color);
        return t;
    }

    private LinearLayout.LayoutParams margin(int l, int top, int r, int bottom) {
        LinearLayout.LayoutParams p = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        p.setMargins(dp(l), dp(top), dp(r), dp(bottom));
        return p;
    }

    private int dp(int v) {
        return (int) (v * context.getResources().getDisplayMetrics().density + 0.5f);
    }
}
