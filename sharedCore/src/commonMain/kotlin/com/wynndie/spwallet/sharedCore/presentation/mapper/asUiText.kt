package com.wynndie.spwallet.sharedCore.presentation.mapper

import com.wynndie.spwallet.sharedCore.domain.error.DataError
import com.wynndie.spwallet.sharedCore.domain.error.Error
import com.wynndie.spwallet.sharedCore.domain.error.ValidationError
import com.wynndie.spwallet.sharedCore.presentation.model.UiText
import com.wynndie.spwallet.sharedResources.Res
import com.wynndie.spwallet.sharedResources.error_above_maximum_value
import com.wynndie.spwallet.sharedResources.error_bad_request
import com.wynndie.spwallet.sharedResources.error_below_minimum_value
import com.wynndie.spwallet.sharedResources.error_empty_field
import com.wynndie.spwallet.sharedResources.error_empty_table
import com.wynndie.spwallet.sharedResources.error_forbidden
import com.wynndie.spwallet.sharedResources.error_internal_server_error
import com.wynndie.spwallet.sharedResources.error_invalid_characters
import com.wynndie.spwallet.sharedResources.error_no_internet
import com.wynndie.spwallet.sharedResources.error_no_match_found
import com.wynndie.spwallet.sharedResources.error_not_enough_money
import com.wynndie.spwallet.sharedResources.error_not_found
import com.wynndie.spwallet.sharedResources.error_request_timeout
import com.wynndie.spwallet.sharedResources.error_serialization
import com.wynndie.spwallet.sharedResources.error_unauthorized
import com.wynndie.spwallet.sharedResources.error_unknown

fun Error.asUiText(): UiText {
    return when (this) {
        DataError.Remote.BAD_REQUEST -> UiText.StringResourceId(Res.string.error_bad_request)
        DataError.Remote.UNAUTHORIZED -> UiText.StringResourceId(Res.string.error_unauthorized)
        DataError.Remote.FORBIDDEN -> UiText.StringResourceId(Res.string.error_forbidden)
        DataError.Remote.NOT_FOUND -> UiText.StringResourceId(Res.string.error_not_found)
        DataError.Remote.REQUEST_TIMEOUT -> UiText.StringResourceId(Res.string.error_request_timeout)
        DataError.Remote.TOO_MANY_REQUESTS -> UiText.StringResourceId(Res.string.error_unknown)
        DataError.Remote.SERVER_ERROR -> UiText.StringResourceId(Res.string.error_internal_server_error)
        DataError.Remote.NO_INTERNET -> UiText.StringResourceId(Res.string.error_no_internet)
        DataError.Remote.SERIALIZATION_ERROR -> UiText.StringResourceId(Res.string.error_serialization)
        DataError.Remote.UNKNOWN -> UiText.StringResourceId(Res.string.error_unknown)

        DataError.Local.DISK_FULL -> UiText.StringResourceId(Res.string.error_unknown)
        DataError.Local.EMPTY_TABLE -> UiText.StringResourceId(Res.string.error_empty_table)
        DataError.Local.NO_MATCH_FOUND -> UiText.StringResourceId(Res.string.error_no_match_found)

        ValidationError.EMPTY_FIELD -> UiText.StringResourceId(Res.string.error_empty_field)
        ValidationError.BELOW_MINIMUM -> UiText.StringResourceId(Res.string.error_below_minimum_value)
        ValidationError.ABOVE_MAXIMUM -> UiText.StringResourceId(Res.string.error_above_maximum_value)
        ValidationError.EXACT_VALUE_REQUIRED -> UiText.StringResourceId(Res.string.error_above_maximum_value)
        ValidationError.INVALID_CHARACTERS -> UiText.StringResourceId(Res.string.error_invalid_characters)
        ValidationError.INSUFFICIENT_VALUE -> UiText.StringResourceId(Res.string.error_not_enough_money)
    }
}