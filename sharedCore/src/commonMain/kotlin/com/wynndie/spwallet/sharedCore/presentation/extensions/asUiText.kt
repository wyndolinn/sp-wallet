package com.wynndie.spwallet.sharedCore.presentation.extensions

import com.wynndie.spwallet.sharedCore.domain.outcome.Error
import com.wynndie.spwallet.sharedCore.presentation.formatters.UiText
import com.wynndie.spwallet.sharedResources.Res
import com.wynndie.spwallet.sharedResources.error_above_maximum_value
import com.wynndie.spwallet.sharedResources.error_bad_request
import com.wynndie.spwallet.sharedResources.error_below_minimum_value
import com.wynndie.spwallet.sharedResources.error_empty_field
import com.wynndie.spwallet.sharedResources.error_forbidden
import com.wynndie.spwallet.sharedResources.error_internal_server_error
import com.wynndie.spwallet.sharedResources.error_invalid_characters
import com.wynndie.spwallet.sharedResources.error_no_internet
import com.wynndie.spwallet.sharedResources.error_not_found
import com.wynndie.spwallet.sharedResources.error_request_timeout
import com.wynndie.spwallet.sharedResources.error_serialization
import com.wynndie.spwallet.sharedResources.error_unauthorized
import com.wynndie.spwallet.sharedResources.error_unknown

fun Error.asUiText(): UiText {
    return when (this) {
        Error.Network.BAD_REQUEST -> UiText.ResourceString(Res.string.error_bad_request)
        Error.Network.UNAUTHORIZED -> UiText.ResourceString(Res.string.error_unauthorized)
        Error.Network.FORBIDDEN -> UiText.ResourceString(Res.string.error_forbidden)
        Error.Network.NOT_FOUND -> UiText.ResourceString(Res.string.error_not_found)
        Error.Network.REQUEST_TIMEOUT -> UiText.ResourceString(Res.string.error_request_timeout)
        Error.Network.TOO_MANY_REQUESTS -> UiText.ResourceString(Res.string.error_unknown)
        Error.Network.SERVER_ERROR -> UiText.ResourceString(Res.string.error_internal_server_error)
        Error.Network.NO_INTERNET -> UiText.ResourceString(Res.string.error_no_internet)
        Error.Network.SERIALIZATION_ERROR -> UiText.ResourceString(Res.string.error_serialization)
        Error.Network.UNKNOWN -> UiText.ResourceString(Res.string.error_unknown)

        Error.Validation.EMPTY_FIELD -> UiText.ResourceString(Res.string.error_empty_field)
        Error.Validation.BELOW_MINIMUM_VALUE -> UiText.ResourceString(Res.string.error_below_minimum_value)
        Error.Validation.ABOVE_MAXIMUM_VALUE -> UiText.ResourceString(Res.string.error_above_maximum_value)
        Error.Validation.EXACT_VALUE_REQUIRED -> UiText.ResourceString(Res.string.error_above_maximum_value)
        Error.Validation.INVALID_CHARACTERS -> UiText.ResourceString(Res.string.error_invalid_characters)
        Error.Validation.INVALID_FORMAT -> UiText.ResourceString(Res.string.error_unknown)
        Error.Validation.BELOW_MINIMUM_LENGTH -> UiText.ResourceString(Res.string.error_unknown)
        Error.Validation.ABOVE_MAXIMUM_LENGTH -> UiText.ResourceString(Res.string.error_unknown)
        Error.Validation.EXACT_LENGTH_REQUIRED -> UiText.ResourceString(Res.string.error_unknown)
    }
}