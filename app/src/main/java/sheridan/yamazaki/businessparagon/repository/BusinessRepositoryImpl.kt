package sheridan.yamazaki.businessparagon.repository

import android.app.Application
import android.icu.lang.UCharacter.GraphemeClusterBreak.T
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
//import sheridan.yamazaki.businessparagon.firestore.FirestoreCollectionLiveData
//import sheridan.yamazaki.businessparagon.firestore.FirestoreDocumentLiveData
import sheridan.yamazaki.businessparagon.model.Business
//import sheridan.yamazaki.businessparagon.util.RatingUtil
//import sheridan.yamazaki.businessparagon.util.RestaurantUtil
//import com.google.firebase.firestore.*
//import com.google.firebase.firestore.ktx.firestore
//import com.google.firebase.firestore.ktx.toObject
//import com.google.firebase.ktx.Firebase
import javax.inject.Inject

class BusinessRepositoryImpl @Inject constructor(
    private val application: Application
) : BusinessRepository {

    companion object {
        private const val TAG = "BusinessRepositoryImpl"
        private const val LIMIT = 50
    }

   /* private val firestore = Firebase.firestore
    private val collection = firestore.collection("restaurants")
    private val query = collection.orderBy("avgRating", Query.Direction.DESCENDING)
        .limit(LIMIT.toLong())*/

    override fun getAllBusiness(): LiveData<List<Business>> {
        return createDummyBusinesses()
       // return FirestoreCollectionLiveData(query, Business::class.java)
    }

    /*override fun getBusiness(id: String): LiveData<Business> {
        //return FirestoreDocumentLiveData(collection.document(id), Business::class.java)
    }*/

    fun createDummyBusinesses(): LiveData<List<Business>> {
        val list3 = listOf(
        Business("Loblaw","Grocery", "loblaws.ca", "647-666-666", "123 Main st. Mississauga", "image", 1),
        Business("Walmart","Grocery", "walmart.ca", "416-555-666", "55 Queen st. Oakville", "image", 2),
        Business("Best Buy","Electronics", "bestbuy.ca", "647-999-999", "89 Jarvis st. Toronto", "image", 3),
        Business("Burger King","Restaurant", "burgerking.ca", "647-123-123", "899 King st. Mississauga", "image", 4),
        Business("Nandos","Restaurant", "nandos.ca", "416-455-213", "13 Brittania. Mississauga", "image", 5))

        return  MutableLiveData(list3)

    }

    override fun loadRandomData() {
        Log.d("j", "k")
    }
    }
    /*override fun loadRandomData() {
        // Add a bunch of random restaurants
        val batch = firestore.batch()
        for (i in 0..9) {
            val restRef = firestore.collection("restaurants").document()

            // Create random restaurant / ratings
            val randomRestaurant = RestaurantUtil.getRandom(application)
            val randomRatings = RatingUtil.getRandomList(randomRestaurant.numRatings)
            randomRestaurant.avgRating = RatingUtil.getAverageRating(randomRatings)

            // Add restaurant
            batch.set(restRef, randomRestaurant)

            // Add ratings to sub-collection
            for (rating in randomRatings) {
                batch.set(restRef.collection("ratings").document(), rating)
            }
        }

        batch.commit().addOnCompleteListener { task ->
            if (task.isSuccessful) {
                Log.d(TAG, "Write batch succeeded.")
            } else {
                Log.w(TAG, "write batch failed.", task.exception)
            }
        }
    }*/
