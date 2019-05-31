package com.binhnk.sample.imagetovideo.base

import androidx.lifecycle.ViewModel
import io.reactivex.disposables.CompositeDisposable

abstract class BaseViewModel  : ViewModel() {

    var compositeDisposable: CompositeDisposable = CompositeDisposable()

    override fun onCleared() {
        compositeDisposable.dispose()
//        useCases.let { userCases ->
//            if (userCases.isNotEmpty()) userCases.forEach { it?.onCleared() }
//        }
        super.onCleared()
    }
}
