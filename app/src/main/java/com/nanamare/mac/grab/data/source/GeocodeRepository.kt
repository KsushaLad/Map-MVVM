package com.nanamare.mac.grab.data.source

import android.location.Address
import com.nanamare.mac.grab.data.source.remote.RemoteGeocodeDataSourceImpl
import com.nanamare.mac.grab.network.response.GeocodeResponse
import com.nanamare.mac.grab.network.response.PlaceResponse
import com.nanamare.mac.grab.network.response.ReverseGeocodeResponse
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers

class GeocodeRepository(private val remoteGeocodeDataSourceImpl: RemoteGeocodeDataSourceImpl) :
    GeocodeDataSource {

    override fun getLocationUseAddress(address: String): Single<GeocodeResponse> =
        remoteGeocodeDataSourceImpl.getLocationUseAddress(address)

    override fun getLocationUseLatLng(latLng: String): Single<ReverseGeocodeResponse> =
        remoteGeocodeDataSourceImpl.getLocationUseLatLng(latLng)

    override fun getPlace(address: String): Single<PlaceResponse> =
        remoteGeocodeDataSourceImpl.getPlace(address)


}