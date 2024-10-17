package com.example.authtutorial.screens.login


import android.app.Application
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.authtutorial.AuthApplication
import com.example.authtutorial.repository.APIResource
import com.example.authtutorial.network.auth0.AuthorizeResponse
import com.example.authtutorial.repository.IAuthRepo
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class LoginViewModel(val login : (AuthorizeResponse)->Unit, val authRepo: IAuthRepo): ViewModel() {
    private val _uiState = MutableStateFlow(LoginState())
    val uiState : StateFlow<LoginState> = _uiState.asStateFlow()


    /*The viewmodel exposes 1 stateflow that holds a possible API response*/
    /*
    * There is no use in calling the API when the user input isn't ready,
    * therefore the flow starts of by emitting a Success state
    *
    * Then, in the submit method, the flow is replaced by a new one, that hooks in on the repo
    * */
    private val _authResponse = MutableStateFlow(
        flow<APIResource<AuthorizeResponse>>{  APIResource.Success(null) })

    val authResponse : StateFlow<APIResource<AuthorizeResponse>> = _authResponse
        .flatMapLatest {
            it
        }.onEach{ resource ->
            Log.i("FLOW", "observed: " + resource.data)
            if(resource is APIResource.Success && resource.data != null)
                login(resource.data)
            else if(resource is APIResource.Error<*>){
                _uiState.value = _uiState.value.copy(loginError = true)
                flow { emit(APIResource.Error(resource.message ?: "", resource.data))}
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = APIResource.Success(null)
        )



    fun updateUserName(name: String){
        resetError()
        _uiState.value = _uiState.value.copy(
            username = name
        )
    }

    fun updatePwd(pwd: String){
        resetError()
        _uiState.value = _uiState.value.copy(
            password = pwd
        )
    }
    private fun resetError(){
        _uiState.value = _uiState.value.copy(loginError = false)
    }

    fun onSubmit() {
        //Change the flow to fire the API call
        viewModelScope.launch {
            _authResponse.value = authRepo.getToken(userName = uiState.value.username, password = uiState.value.password)

        }

    }






    companion object {
        private var Instance: LoginViewModel? = null
        val LOGIN_KEY = object : CreationExtras.Key<(AuthorizeResponse)->Unit> {}
        val APPLICATION_KEY = object: CreationExtras.Key<Application>{}

        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {

                var login = this[LOGIN_KEY] as (AuthorizeResponse)->Unit
                val application = this[APPLICATION_KEY] as AuthApplication
                if (Instance == null) {

                    val authRepo = application.container.authRepo
                    Instance = LoginViewModel(login = login, authRepo = authRepo)
                }
                Instance!!

            }
        }
    }
}