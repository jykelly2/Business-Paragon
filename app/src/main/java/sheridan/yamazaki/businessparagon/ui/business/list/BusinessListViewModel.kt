package sheridan.yamazaki.businessparagon.ui.business.list

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import sheridan.yamazaki.businessparagon.model.Business
import sheridan.yamazaki.businessparagon.repository.BusinessRepository

class BusinessListViewModel @ViewModelInject constructor(
        private val repository: BusinessRepository
): ViewModel() {

    val businesses: LiveData<List<Business>> = repository.getAllBusiness()

    fun loadRandomData() {
        repository.loadRandomData()
    }
}
/*class BusinessListViewModel (
    application: Application
) : AndroidViewModel(application){

    //private val repository =  BusinessRepository

    val businesses: LiveData<List<Business>> = getAllBusiness()

    fun loadRandomData() {
        repository.loadRandomData()
    }

    fun getAllBusiness() : LiveData<List<Business>> {
        val list3 = listOf(
                Business("Loblaw","Grocery", "loblaws.ca", "647-666-666", "123 Main st. Mississauga", "image", 1),
                Business("Walmart","Grocery", "walmart.ca", "416-555-666", "55 Queen st. Oakville", "image", 2),
                Business("Best Buy","Electronics", "bestbuy.ca", "647-999-999", "89 Jarvis st. Toronto", "image", 3),
                Business("Burger King","Restaurant", "burgerking.ca", "647-123-123", "899 King st. Mississauga", "image", 4),
                Business("Nandos","Restaurant", "nandos.ca", "416-455-213", "13 Brittania. Mississauga", "image", 5))

        return  MutableLiveData(list3)
    }
   class Factory(val app: Application) : ViewModelProvider.Factory {
       override fun <T : ViewModel?> create(modelClass: Class<T>): T {
           if (modelClass.isAssignableFrom(BusinessListViewModel::class.java)) {
               @Suppress("UNCHECKED_CAST")
               return BusinessListViewModel(app) as T
           }
           throw IllegalArgumentException("Unable to construct viewmodel")
       }
   }
}*/