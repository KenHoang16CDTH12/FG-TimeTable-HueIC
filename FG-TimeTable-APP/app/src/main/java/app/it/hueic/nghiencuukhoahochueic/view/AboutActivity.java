package app.it.hueic.nghiencuukhoahochueic.view;

import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.FrameLayout;

import com.vansuita.materialabout.builder.AboutBuilder;
import com.vansuita.materialabout.views.AboutView;

import app.it.hueic.nghiencuukhoahochueic.R;

public class AboutActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        setTheme(R.style.AppThemeDark);
        AboutView view = AboutBuilder.with(this)
                .setAppIcon(R.mipmap.ic_launcher_round)
                .setAppName(R.string.app_name)
                .setPhoto(R.drawable.ashitaka)
                .setCover(R.mipmap.profile_cover)
                .setLinksAnimated(true)
                .setDividerDashGap(13)
                .setName("Ken Hoang")
                .setSubTitle("Android Developer")
                .setLinksColumnsCount(4)
                .setBrief("I'm a mobile developer, student HueIC and member of club HueIC-IT.")
                .addGooglePlayStoreLink("null")
                .addGitHubLink("KenHoang16CDTH12")
                .addFacebookLink("100009340086453")
                .addGooglePlusLink("103009092046378168360")
                .addYoutubeChannelLink("UCrqd61txir2ehVHEje2eUPg")
                .addEmailLink("hoang.duongminh0221@gmail.com")
                .addSkypeLink("01232954563")
                .addGoogleLink("hoang.duongminh0221@gmail.com")
                .addFiveStarsAction()
                .addMoreFromMeAction("Ken Hoang")
                .setVersionNameAsAppSubTitle()
                .addShareAction(R.string.app_name)
                .addUpdateAction()
                .setActionsColumnsCount(2)
                .addFeedbackAction("hoang.duongminh0221@gmail.com")
                .addIntroduceAction((Intent) null)
                .addHelpAction((Intent) null)
                .addDonateAction((Intent) null)
                .setWrapScrollView(true)
                .setShowAsCard(true)
                .build();
        FrameLayout frameLayout = findViewById(R.id.frameLayout);
        frameLayout.addView(view);
    }
}
