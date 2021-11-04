import androidx.lifecycle.*
import com.example.theweather.domain.models.WeatherModel
import com.example.theweather.domain.usecase.AddNewWeatherModelUseCase
import com.example.theweather.domain.usecase.GetCurrentWeatherModelUseCase
import com.example.theweather.utils.DebugConsole
import com.example.theweather.utils.TemperatureUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import javax.inject.Inject

class MainViewModel @Inject constructor(
    private val getCurrentWeatherModelUseCase: GetCurrentWeatherModelUseCase,
    private val addNewWeatherModelUseCase: AddNewWeatherModelUseCase
) :
    ViewModel() {

    private val temperatureUnitsTypeMutable =
        MutableLiveData<TemperatureUtils.TemperatureUnitsType>()
    val temperatureUnitsType: LiveData<TemperatureUtils.TemperatureUnitsType> =
        temperatureUnitsTypeMutable


    fun switchToCelsius() {
        temperatureUnitsTypeMutable.value = TemperatureUtils.TemperatureUnitsType.CELSIUS
    }

    fun switchToFahrenheit() {
        temperatureUnitsTypeMutable.value = TemperatureUtils.TemperatureUnitsType.FAHRENHEIT
    }

    fun getCurrentWeather() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val currentWeather = getCurrentWeatherAsync()
                addWeatherToStorage(currentWeather)
            } catch (exception: Exception) {
                DebugConsole.error(
                    this,
                    exception.message ?: "getCurrentWeather: something went wrong!"
                )
            }
        }
    }

    private suspend fun getCurrentWeatherAsync(): WeatherModel {

        val model = viewModelScope.async(Dispatchers.IO) { getCurrentWeatherModelUseCase.execute() }
        return model.await()
    }

    private suspend fun addWeatherToStorage(weatherModel: WeatherModel) {
        addNewWeatherModelUseCase.execute(weatherModel)
    }

    class Factory @Inject constructor(
        private val getCurrentWeatherModelUseCase: GetCurrentWeatherModelUseCase,
        private val addNewWeatherModelUseCase: AddNewWeatherModelUseCase
    ) :
        ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return MainViewModel(
                getCurrentWeatherModelUseCase,
                addNewWeatherModelUseCase
            ) as T;
        }
    }
}