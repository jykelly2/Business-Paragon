package sheridan.yamazaki.businessparagon


//import com.google.firebase.FirebaseApp
import android.app.Application
import com.google.firebase.FirebaseApp
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.HiltAndroidApp


@HiltAndroidApp
class BusinessParagonApplication : Application(){
  /*  override fun onCreate() {
        super.onCreate()
        FirebaseFirestore.setLoggingEnabled(true)
        FirebaseApp.initializeApp(this.applicationContext)
    }*/
}