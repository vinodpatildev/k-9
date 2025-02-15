package app.k9mail.feature.onboarding.main.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.navigation.NavController
import androidx.navigation.NavOptionsBuilder
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import app.k9mail.feature.account.setup.navigation.AccountSetupNavHost
import app.k9mail.feature.onboarding.permissions.domain.PermissionsDomainContract.UseCase.HasRuntimePermissions
import app.k9mail.feature.onboarding.permissions.ui.PermissionsScreen
import app.k9mail.feature.onboarding.welcome.ui.WelcomeScreen
import org.koin.compose.koinInject

private const val NESTED_NAVIGATION_ROUTE_WELCOME = "welcome"
private const val NESTED_NAVIGATION_ROUTE_ACCOUNT_SETUP = "account_setup"
private const val NESTED_NAVIGATION_ROUTE_PERMISSIONS = "permissions"

private fun NavController.navigateToAccountSetup() {
    navigate(NESTED_NAVIGATION_ROUTE_ACCOUNT_SETUP)
}

private fun NavController.navigateToPermissions(builder: NavOptionsBuilder.() -> Unit) {
    navigate(NESTED_NAVIGATION_ROUTE_PERMISSIONS, builder)
}

@Composable
fun OnboardingNavHost(
    onImport: () -> Unit,
    onFinish: (String) -> Unit,
    hasRuntimePermissions: HasRuntimePermissions = koinInject(),
) {
    val navController = rememberNavController()
    var accountUuid by rememberSaveable { mutableStateOf<String?>(null) }

    NavHost(
        navController = navController,
        startDestination = NESTED_NAVIGATION_ROUTE_WELCOME,
    ) {
        composable(route = NESTED_NAVIGATION_ROUTE_WELCOME) {
            WelcomeScreen(
                onStartClick = { navController.navigateToAccountSetup() },
                onImportClick = onImport,
            )
        }

        composable(route = NESTED_NAVIGATION_ROUTE_ACCOUNT_SETUP) {
            AccountSetupNavHost(
                onBack = { navController.popBackStack() },
                onFinish = { createdAccountUuid: String ->
                    accountUuid = createdAccountUuid
                    if (hasRuntimePermissions()) {
                        navController.navigateToPermissions {
                            popUpTo(NESTED_NAVIGATION_ROUTE_WELCOME) {
                                inclusive = true
                            }
                        }
                    } else {
                        onFinish(createdAccountUuid)
                    }
                },
            )
        }

        composable(route = NESTED_NAVIGATION_ROUTE_PERMISSIONS) {
            PermissionsScreen(
                onNext = { onFinish(requireNotNull(accountUuid)) },
            )
        }
    }
}
