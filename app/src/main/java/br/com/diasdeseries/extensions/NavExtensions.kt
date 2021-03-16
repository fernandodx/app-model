package br.com.diasdeseries.extensions

import androidx.navigation.NavController
import androidx.navigation.NavDirections
import androidx.navigation.NavOptions
import br.com.diasdeseries.R

private val slideLeftoptions = NavOptions.Builder()
    .setEnterAnim(R.anim.slide_in_right)
    .setExitAnim(R.anim.slide_out_left)
    .setPopEnterAnim(R.anim.slide_in_left)
    .setPopExitAnim(R.anim.slide_out_right)
    .build()

fun NavController.navigateWithAnimations(
    destinationId: Int,
    animation: NavOptions = slideLeftoptions
){
    this.navigate(destinationId, null, animation)
}

fun NavController.navigateWithAnimations(
    destinationId: NavDirections,
    animation: NavOptions = slideLeftoptions
){
    this.navigate(destinationId, animation)
}