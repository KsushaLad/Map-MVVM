package com.nanamare.mac.grab.vm

import android.view.View
import android.view.inputmethod.EditorInfo
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.nanamare.mac.grab.base.ui.BaseViewModel
import com.nanamare.mac.grab.data.source.GeocodeRepository
import com.nanamare.mac.grab.data.source.LocalSearchPlaceRepository
import com.nanamare.mac.grab.data.source.SearchPlaceDataSourceFactory
import com.nanamare.mac.grab.data.source.vo.LocationVO
import com.nanamare.mac.grab.network.NetworkState
import com.nanamare.mac.grab.network.response.PlaceResponse

class SearchLocationViewModel(
    private val geocodeRepository: GeocodeRepository,
    private val localSearchPlaceRepository: LocalSearchPlaceRepository
) : BaseViewModel() {

    var liveSearchItems =
        MutableLiveData<LiveData<PagedList<PlaceResponse.Result>>>()

    val liveLocalLocations = MutableLiveData<List<LocationVO>>()

    private val _livePlaceState = MutableLiveData<NetworkState<PlaceResponse>>()
    val livePlaceState: LiveData<NetworkState<PlaceResponse>> get() = _livePlaceState

    val liveKeyword = MutableLiveData<String>()

    private val _liveIsResultZero = MutableLiveData<Boolean>()
    val liveIsResultZero: LiveData<Boolean> get() = _liveIsResultZero

    fun onSearchClick() {
        _liveIsResultZero.value = false
        liveSearchItems.value = LivePagedListBuilder(
            SearchPlaceDataSourceFactory(
                geocodeRepository,
                liveKeyword.value ?: error("empty keyword"),
                _livePlaceState,
                compositeDisposable
            ), 10
        ).setBoundaryCallback(object : PagedList.BoundaryCallback<PlaceResponse.Result>() {
            override fun onZeroItemsLoaded() {
                super.onZeroItemsLoaded()
                _liveIsResultZero.value = true
            }
        }).build()
    }

//    fun setUpSearchView() { //настройка представление поиска
//        searchView = findViewById(R.id.search)
//        searchView?.setOnEditorActionListener { v, actionId, _ ->
//            var handled = false //обработка
//            if (actionId == EditorInfo.IME_ACTION_SEARCH && v.text.toString().isNotEmpty()
//            ) {
//                retrieveLocationFrom(v.text.toString())
//                closeKeyboard()
//                handled = true
//            }
//            handled
//        }
//        textWatcher = searchTextWatcher
//        searchView?.addTextChangedListener(textWatcher)
//        if (!isLegacyLayoutEnabled) {
//            searchView?.setOnFocusChangeListener { _: View?, hasFocus: Boolean ->
//                if (hasFocus) {
//                    showSearchLayout()
//                }
//            }
//        }
//    }

//    fun retrieveLocationFrom(query: String) { //восстановление местоположения из строки
//        if (searchZone != null && searchZone!!.isNotEmpty()) {
//            retrieveLocationFromDefaultZone(query)
//        }
//        else if (isSearchZoneWithDefaultLocale) {
//            retrieveLocationFromDefaultZone(query)
//        } else {
//            geocoderPresenter?.getFromLocationName(query)
//        }
//    }


    fun saveLocations(locationVO: LocationVO) {
        localSearchPlaceRepository.saveLocationVOList(locationVO) {
            // saved callback
        }
    }

    fun loadLocalLocations() {
        liveLocalLocations.value = localSearchPlaceRepository.loadLocationVOList()?.reversed()
    }

    fun clearLocalLocations() {
        localSearchPlaceRepository.clearLocationVOList {
            liveLocalLocations.value = emptyList()
        }
    }

}
