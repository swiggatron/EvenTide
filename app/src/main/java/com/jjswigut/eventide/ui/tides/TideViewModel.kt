package com.jjswigut.eventide.ui.tides

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.map
import com.jjswigut.eventide.data.entities.DayHeader
import com.jjswigut.eventide.data.entities.UIModel
import com.jjswigut.eventide.data.entities.tidalpredictions.Prediction
import com.jjswigut.eventide.data.repository.TideRepository
import com.jjswigut.eventide.utils.Preferences
import com.jjswigut.eventide.utils.Resource

class TideViewModel @ViewModelInject constructor(
    private val repo: TideRepository,
    val prefs: Preferences
) : ViewModel() {

    val sortedTidesLiveData: LiveData<ArrayList<UIModel>> =
        getTidesWithLocation(prefs.nearestStationId!!).map {
            sortTides(it.data)
        }

    private fun sortTides(list: List<Prediction>?): ArrayList<UIModel> =
        arrayListOf<UIModel>().apply {
            list?.groupBy { it.t.take(10) }?.forEach { (day, predictions) ->
                add(UIModel.DayModel(DayHeader(day)))
                predictions.forEach { add(UIModel.TideModel(it)) }
            }
        }

    private fun getTidesWithLocation(station: String): LiveData<Resource<List<Prediction>>> =
        repo.getTides(station)
}





