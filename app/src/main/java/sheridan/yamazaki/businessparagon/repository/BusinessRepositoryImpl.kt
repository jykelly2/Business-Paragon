package sheridan.yamazaki.businessparagon.repository

//import sheridan.yamazaki.businessparagon.util.RestaurantUtil
//import com.google.firebase.ktx.Firebase

import android.app.Application
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.tasks.await
import sheridan.yamazaki.businessparagon.firestore.FirestoreCollectionLiveData
import sheridan.yamazaki.businessparagon.firestore.FirestoreDocumentLiveData
import sheridan.yamazaki.businessparagon.model.Business
import sheridan.yamazaki.businessparagon.model.Layout
import javax.inject.Inject


class BusinessRepositoryImpl @Inject constructor(
    private val application: Application
) : BusinessRepository {

    companion object {
        private const val TAG = "BusinessRepositoryImpl"
        private const val LIMIT = 50
    }

    private val firestore = Firebase.firestore
    private val collection = firestore.collection("businesses")
    private val query = collection.orderBy("name", Query.Direction.ASCENDING)
            .limit(LIMIT.toLong())

    override fun getAllBusiness(): LiveData<List<Business>> {
        return FirestoreCollectionLiveData(query, Business::class.java)
    }

    override fun getBusiness(id: String): LiveData<Business> {
        return FirestoreDocumentLiveData(collection.document(id), Business::class.java)
    }

    override suspend fun getBusinessLayout(id: String, layoutName: String): LiveData<Layout> {
        val design = collection.document(id).collection("design")
                .whereEqualTo("name", layoutName).limit(1).get().await().documents[0].toObject<Layout>()

        return MutableLiveData(design)
    }

    /* fun createDummyBusinesses(): LiveData<List<Business>> {
        val list3 = listOf(
        Business("Loblaw","Grocery", "loblaws.ca", "647-666-666", "123 Main st. Mississauga", "https://img.huffingtonpost.com/asset/5e1512f2250000ffddd3214c.jpeg?cache=jOcO5DLOgn&ops=1200_630", 1),
        Business("Walmart","Grocery", "walmart.ca", "416-555-666", "55 Queen st. Oakville", "https://www.supermarketnews.com/sites/supermarketnews.com/files/styles/article_featured_retina/public/Walmart_Canada_supercenter_exterior.png?itok=lVSg6uOM", 2),
        Business("Best Buy","Electronics", "bestbuy.ca", "647-999-999", "89 Jarvis st. Toronto", "https://www.retail-insider.com/wp-content/uploads/2020/11/1280x720-1068x601.jpg", 3),
        Business("Burger King","Restaurant", "burgerking.ca", "647-123-123", "899 King st. Mississauga", "https://b.zmtcdn.com/data/pictures/chains/5/16500625/9b0d338b5e4a9f82e5c2609df4592864_featured_v2.jpg?fit=around|771.75:416.25&crop=771.75:416.25;*,*", 4),
        Business("Nandos","Restaurant", "nandos.ca", "416-455-213", "13 Brittania. Mississauga", "https://d1ralsognjng37.cloudfront.net/ea6e9704-2eba-4bb7-b74a-4f0f7cfcca53.jpeg", 5))

        return  MutableLiveData(list3)

    }*/
}


