package com.example.googlecastpersonalizada;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.view.Menu;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.cast.Cast;
import com.google.android.gms.cast.CastDevice;
import com.google.android.gms.cast.framework.CastButtonFactory;
import com.google.android.gms.cast.framework.CastContext;
import com.google.android.gms.cast.framework.CastSession;
import com.google.android.gms.cast.framework.Session;
import com.google.android.gms.cast.framework.SessionManager;
import com.google.android.gms.cast.framework.SessionManagerListener;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;

import java.io.IOException;

public class ActividadPrincipal extends AppCompatActivity {

    CanalCast mCanalCast = new CanalCast();
    private CastSession mCastSession;
    private SessionManager mSessionManager;
    private Button btnSendMessage, btnBackgroundBlue, btnBackgroundRed, btnBackgroundYellow,
            btnBackgroundwhite, btnBackgroundBlack;
    private EditText etMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        CastContext castContext = CastContext.getSharedInstance(this);

        mSessionManager = castContext.getSessionManager();
        etMessage = (EditText) findViewById(R.id.et_input);
        btnSendMessage = (Button) findViewById(R.id.btn_send_message);
        btnBackgroundBlue = (Button) findViewById(R.id.btn_blue_background);
        btnBackgroundRed = (Button) findViewById(R.id.btn_red_background);
        btnBackgroundYellow = (Button) findViewById(R.id.btn_yellow_background);
        btnBackgroundwhite = (Button) findViewById(R.id.btn_white_background);
        btnBackgroundBlack = (Button) findViewById(R.id.btn_black_background);
        btnSendMessage.setOnClickListener(btnClickListener);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.menu_actividad_principal, menu);
        CastButtonFactory.setUpMediaRouteButton(getApplicationContext(), menu, R.id.media_route_menu_item);
        return true;
    }

    private final View.OnClickListener btnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.btn_send_message:
                    final String message = etMessage.getText().toString();
                    if (TextUtils.isEmpty(message)) {
                        Toast.makeText(ActividadPrincipal.this, "Ingresa un mensaje", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    sendMessage("#T#"+message);
                    etMessage.setText("");
                    etMessage.requestFocus();
                    break;
                case R.id.btn_blue_background:
                    sendMessage("#F#blue");
                    break;
                case R.id.btn_red_background:
                    sendMessage("#F#red");
                    break;
                case R.id.btn_yellow_background:
                    sendMessage("#F#yellow");
                    break;
                case R.id.btn_white_background:
                    sendMessage("#F#white");
                    break;
                case R.id.btn_black_background:
                    sendMessage("#F#black");
                    break;
            }
        }
    };

    private final SessionManagerListener mSessionManagerListener = new SessionManagerListenerImpl();

    private class SessionManagerListenerImpl implements SessionManagerListener {
        @Override
        public void onSessionStarted(Session session, String sessionId) {
            invalidateOptionsMenu();
            setSessionStarted(true);
            mCastSession = mSessionManager.getCurrentCastSession();

            if (mCastSession != null && mCanalCast == null) {
                mCanalCast = new CanalCast();
                try {
                    mCastSession.setMessageReceivedCallbacks(mCanalCast.getNamespace(), mCanalCast);
                } catch (IOException e) {
                    mCanalCast = null;
                }
            }
        }

        @Override
        public void onSessionResumed(Session session, boolean wasSuspended) {
            invalidateOptionsMenu();
            setSessionStarted(true);
        }

        @Override
        public void onSessionSuspended(Session session, int error) {
            setSessionStarted(false);
        }

        @Override
        public void onSessionStarting(Session session) {
        }

        @Override
        public void onSessionResuming(Session session, String sessionId) {
        }

        @Override
        public void onSessionStartFailed(Session session, int error) {
        }

        @Override
        public void onSessionResumeFailed(Session session, int error) {
        }

        @Override
        public void onSessionEnding(Session session) {
        }

        @Override
        public void onSessionEnded(Session session, int error) {
            setSessionStarted(false);
        }
    }

    private void setSessionStarted(boolean enabled) {
        etMessage.setEnabled(enabled);
        btnSendMessage.setEnabled(enabled);
        btnBackgroundBlue.setEnabled(enabled);
        btnBackgroundRed.setEnabled(enabled);
        btnBackgroundYellow.setEnabled(enabled);
        btnBackgroundwhite.setEnabled(enabled);
        btnBackgroundBlack.setEnabled(enabled);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mSessionManager.addSessionManagerListener(mSessionManagerListener);
        mCastSession = mSessionManager.getCurrentCastSession();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mSessionManager.removeSessionManagerListener(mSessionManagerListener);
        mCastSession = null;
    }

    class CanalCast implements Cast.MessageReceivedCallback {
        public String getNamespace() {
            return "urn:x-cast:com.example.googlecastpersonalizada";
        }

        @Override
        public void onMessageReceived(CastDevice castDevice, String namespace, String message) {
            Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG);
        }
    }

    private void sendMessage(String message) {
        if (mCanalCast != null) {
            try {
                mCastSession.sendMessage(mCanalCast.getNamespace(), message).setResultCallback(new ResultCallback<Status>() {
                    @Override
                    public void onResult(Status result) {
                        if (!result.isSuccess()) {
                            Toast.makeText(getApplicationContext(), "Error al enviar el mensaje.", Toast.LENGTH_LONG);
                        }
                    }
                });
            } catch (Exception e) {
                Toast.makeText(getApplicationContext(), "Error al enviar el mensaje: " + e, Toast.LENGTH_LONG);
            }
        }
    }

}
