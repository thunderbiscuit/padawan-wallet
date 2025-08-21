package com.coyotebitcoin.padawanwallet.presentation.navigation

import android.util.Log
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.entry
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.ui.NavDisplay
import com.coyotebitcoin.padawanwallet.domain.bitcoin.Wallet
import com.coyotebitcoin.padawanwallet.domain.utils.WalletCreateType
import com.coyotebitcoin.padawanwallet.presentation.ui.screens.CoreScreens
import com.coyotebitcoin.padawanwallet.presentation.ui.screens.chapters.ChapterScreen
import com.coyotebitcoin.padawanwallet.presentation.ui.screens.chapters.ChaptersRootScreen
import com.coyotebitcoin.padawanwallet.presentation.ui.screens.intro.OnboardingScreen
import com.coyotebitcoin.padawanwallet.presentation.ui.screens.intro.WalletRecoveryScreen
import com.coyotebitcoin.padawanwallet.presentation.ui.screens.settings.AboutScreen
import com.coyotebitcoin.padawanwallet.presentation.ui.screens.settings.LanguagesScreen
import com.coyotebitcoin.padawanwallet.presentation.ui.screens.settings.RecoveryPhraseScreen
import com.coyotebitcoin.padawanwallet.presentation.ui.screens.settings.SendCoinsBackScreen
import com.coyotebitcoin.padawanwallet.presentation.ui.screens.settings.SettingsRootScreen
import com.coyotebitcoin.padawanwallet.presentation.ui.screens.wallet.QrScanScreen
import com.coyotebitcoin.padawanwallet.presentation.ui.screens.wallet.ReceiveScreen
import com.coyotebitcoin.padawanwallet.presentation.ui.screens.wallet.SendScreen
import com.coyotebitcoin.padawanwallet.presentation.ui.screens.wallet.TransactionScreen
import com.coyotebitcoin.padawanwallet.presentation.ui.screens.wallet.WalletHomeScreen
import com.coyotebitcoin.padawanwallet.presentation.viewmodels.ChaptersViewModel
import com.coyotebitcoin.padawanwallet.presentation.viewmodels.ReceiveViewModel
import com.coyotebitcoin.padawanwallet.presentation.viewmodels.WalletViewModel

private const val TAG = "PadawanTag/Navigation"

/**
 * The root navigation composable. This decides whether to show the onboarding flow or the core app screens
 * based on whether the user has completed onboarding. When users navigate to one of the core app screens (wallet,
 * chapters, more), they enter a sub-navigation structure defined in [NavigationCoreScreens].
 */
@Composable
fun NavigationRoot(
    onboardingDone: Boolean
) {
    val startingScreen = if (onboardingDone) SecondaryDestinations.CoreScreens else SecondaryDestinations.OnboardingScreen
    val backStack: NavBackStack = rememberNavBackStack(startingScreen)
    val bottom: NavBackStack = rememberNavBackStack(CoreDestinations.WalletRootScreen)
    val walletViewModel: WalletViewModel = viewModel()
    val receiveViewModel: ReceiveViewModel = viewModel()
    val chaptersViewModel: ChaptersViewModel = viewModel()

    // TODO: Consider scoping the viewmodels to the NavEntries.
    //       https://developer.android.com/guide/navigation/navigation-3/save-state#scoping-viewmodels

    NavDisplay(
        backStack = backStack,
        onBack = { backStack.removeLastOrNull() },
        transitionSpec = {
            fadeIn(tween(250)) togetherWith fadeOut(tween(250))
        },
        popTransitionSpec = {
            fadeIn(tween(250)) togetherWith fadeOut(tween(250))
        },
        entryProvider = entryProvider {
            // ---------------------------------------------------------------------------------------------------------
            // Screens accessible from the onboarding flow
            // ---------------------------------------------------------------------------------------------------------
            entry<SecondaryDestinations.OnboardingScreen> {
                OnboardingScreen(
                    onBuildWallet = ::onBuildWallet,
                    onCreateNav = {
                        backStack.clear()
                        backStack.add(SecondaryDestinations.CoreScreens)
                    },
                    onRecoverNav = { backStack.add(SecondaryDestinations.WalletRecoveryScreen) },
                )
            }

            entry<SecondaryDestinations.WalletRecoveryScreen> {
                WalletRecoveryScreen(
                    onBuildWallet = ::onBuildWallet,
                    onRecoverNav = {
                        backStack.clear()
                        backStack.add(SecondaryDestinations.CoreScreens)
                    },
                )
            }

            // ---------------------------------------------------------------------------------------------------------
            // Core screens
            // This will lead you to the 3 core app screens with bottom navigation, and the sub NavDisplay [NavigationCoreScreens]
            // ---------------------------------------------------------------------------------------------------------
            entry<SecondaryDestinations.CoreScreens> {
                CoreScreens(
                    backStack = backStack,
                    bottomBarBackStack = bottom,
                    walletViewModel = walletViewModel,
                    chaptersViewModel = chaptersViewModel,
                )
            }

            // ---------------------------------------------------------------------------------------------------------
            // Screens accessible from the Wallet tab
            // ---------------------------------------------------------------------------------------------------------
            entry<SecondaryDestinations.TransactionScreen> {
                val txDetails = walletViewModel.getSingleTxDetails()
                if (txDetails != null) {
                    TransactionScreen(
                        txDetails = txDetails,
                        onBack = { backStack.removeLastOrNull() },
                    )
                } else {
                    Log.w(TAG, "Transaction not found")
                }
            }

            entry<SecondaryDestinations.ReceiveScreen> {
                ReceiveScreen(
                    state = receiveViewModel.state,
                    onAction = receiveViewModel::onAction,
                    onBack = { backStack.removeLastOrNull() },
                )
            }

            entry<SecondaryDestinations.SendScreen> {
                SendScreen(
                    state = walletViewModel.walletState,
                    onAction = walletViewModel::onAction,
                    onBack = { backStack.removeLastOrNull() },
                    onQrScreenNavigate = { backStack.add(SecondaryDestinations.QrScanScreen) }
                )
            }

            entry<SecondaryDestinations.QrScanScreen> {
                QrScanScreen(
                    onAction = walletViewModel::onAction,
                    onBack = { backStack.removeLastOrNull() }
                )
            }

            // ---------------------------------------------------------------------------------------------------------
            // Screens accessible from the Learn tab
            // ---------------------------------------------------------------------------------------------------------
            entry<SecondaryDestinations.ChapterScreen> {
                ChapterScreen(
                    state = chaptersViewModel.pageState,
                    onAction = chaptersViewModel::onAction,
                    onBack = { backStack.removeLastOrNull() },
                    onChapterDone = { backStack.removeLastOrNull() }
                )
            }

            // ---------------------------------------------------------------------------------------------------------
            // Screens accessible from the More tab
            // ---------------------------------------------------------------------------------------------------------
            entry<SecondaryDestinations.RecoveryPhraseScreen> {
                RecoveryPhraseScreen(onBack = { backStack.removeLastOrNull() })
            }

            entry<SecondaryDestinations.SendCoinsBackScreen> {
                SendCoinsBackScreen(onBack = { backStack.removeLastOrNull() })
            }

            entry<SecondaryDestinations.LanguagesScreen> {
                LanguagesScreen(onBack = { backStack.removeLastOrNull() })
            }

            entry<SecondaryDestinations.AboutScreen> {
                AboutScreen(onBack = { backStack.removeLastOrNull() })
            }
        }
    )
}

@Composable
fun NavigationCoreScreens(
    backStack: NavBackStack,
    bottomBarBackStack: NavBackStack,
    walletViewModel: WalletViewModel,
    chaptersViewModel: ChaptersViewModel,
    scaffoldPadding: PaddingValues
) {
    // TODO: Consider scoping the viewmodels to the NavEntries.
    //       https://developer.android.com/guide/navigation/navigation-3/save-state#scoping-viewmodels

    NavDisplay(
        backStack = bottomBarBackStack,
        onBack = { },
        transitionSpec = {
            fadeIn(tween(250)) togetherWith fadeOut(tween(250))
        },
        popTransitionSpec = {
            fadeIn(tween(250)) togetherWith fadeOut(tween(250))
        },
        entryProvider = entryProvider {
            entry<CoreDestinations.WalletRootScreen> {
                WalletHomeScreen(
                    walletViewModel.walletState,
                    onAction = walletViewModel::onAction,
                    onNavigation = { destination: SecondaryDestinations ->
                        when (destination) {
                            is SecondaryDestinations.ReceiveScreen     -> backStack.add(destination)
                            is SecondaryDestinations.SendScreen        -> backStack.add(destination)
                            is SecondaryDestinations.TransactionScreen -> backStack.add(destination)
                            else -> Unit
                        }
                    },
                    paddingValues = scaffoldPadding
                )
            }

            entry<CoreDestinations.ChaptersRootScreen> {
                ChaptersRootScreen(
                    state = chaptersViewModel.rootState,
                    onAction = chaptersViewModel::onAction,
                    onChapterNav = { chapter: Int ->
                        backStack.add(SecondaryDestinations.ChapterScreen(chapter))
                    },
                    paddingValues = scaffoldPadding
                )
            }

            entry<CoreDestinations.SettingsRootScreen> {
                SettingsRootScreen(
                    onAction = {  },
                    onNavigation = { destination: SecondaryDestinations ->
                        when (destination) {
                            is SecondaryDestinations.RecoveryPhraseScreen -> backStack.add(destination)
                            is SecondaryDestinations.SendCoinsBackScreen  -> backStack.add(destination)
                            is SecondaryDestinations.LanguagesScreen      -> backStack.add(destination)
                            is SecondaryDestinations.AboutScreen          -> backStack.add(destination)
                            else -> Unit
                        }
                    },
                )
            }
        }
    )
}

fun onBuildWallet(walletCreateType: WalletCreateType) {
    Log.i(TAG, "Building wallet with type: $walletCreateType")
    try {
        when (walletCreateType) {
            is WalletCreateType.FROMSCRATCH -> Wallet.createWallet()
            is WalletCreateType.RECOVER     -> Wallet.recoverWallet(walletCreateType.recoveryPhrase)
        }
    } catch (e: Throwable) {
        Log.i(TAG, "Could not build wallet: $e")
    }
}
