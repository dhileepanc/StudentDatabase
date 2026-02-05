package com.app.studenttask.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import androidx.navigation.NavType
import com.app.studenttask.ui.screens.welcome.WelcomeScreen
import com.app.studenttask.ui.screens.auth.LoginScreen
import com.app.studenttask.ui.screens.auth.SignUpScreen
import com.app.studenttask.ui.screens.home.HomeScreen
import com.app.studenttask.ui.screens.student.AddStudentScreen
import com.app.studenttask.ui.screens.student.StudentListScreen
import com.app.studenttask.ui.screens.map.MapViewScreen
import com.app.studenttask.ui.screens.map.SelectLocationScreen
import com.app.studenttask.ui.screens.student.StudentDetailScreen

sealed class Screen(val route: String) {
    object Welcome : Screen("welcome")
    object Login : Screen("login")
    object SignUp : Screen("signup")
    object Home : Screen("home")
    object AddStudent : Screen("add_student")
    object StudentList : Screen("student_list")
    object MapView : Screen("map_view")
    object SelectLocation : Screen("select_location")
    object StudentDetail : Screen("student_detail/{studentId}") {
        fun createRoute(studentId: Int) = "student_detail/$studentId"
    }
}

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = Screen.Welcome.route) {
        composable(Screen.Welcome.route) {
            WelcomeScreen(
                onNavigateToLogin = { navController.navigate(Screen.Login.route) }
            )
        }
        composable(Screen.Login.route) {
            LoginScreen(
                onLoginSuccess = { navController.navigate(Screen.Home.route) },
                onNavigateToSignUp = { navController.navigate(Screen.SignUp.route) }
            )
        }
        composable(Screen.SignUp.route) {
            SignUpScreen(
                onSignUpSuccess = { navController.popBackStack() }, // Go back to login
                onNavigateToLogin = { navController.popBackStack() }
            )
        }
        composable(Screen.Home.route) {
            HomeScreen(
                onAddStudentClick = { navController.navigate(Screen.AddStudent.route) },
                onViewStudentClick = { navController.navigate(Screen.StudentList.route) },
                onMapClick = { navController.navigate(Screen.MapView.route) },
                onLogout = {
                    navController.navigate(Screen.Login.route) {
                        popUpTo(0)
                    }
                }
            )
        }
        composable(Screen.AddStudent.route) {
            val currentBackStackEntry = navController.currentBackStackEntry
            val savedStateHandle = currentBackStackEntry?.savedStateHandle

            // Observe results from SelectLocationScreen
            val lat = savedStateHandle?.get<Double>("lat")
            val lng = savedStateHandle?.get<Double>("lng")

            // Pass initial values if needed

            AddStudentScreen(
                onBack = { navController.popBackStack() },
                onNavigateToMap = { navController.navigate(Screen.SelectLocation.route) },
                selectedLat = lat,
                selectedLng = lng
            )
        }
        composable(Screen.StudentList.route) {
            StudentListScreen(
                onBack = { navController.popBackStack() },
                onStudentClick = { studentId ->
                    navController.navigate(Screen.StudentDetail.createRoute(studentId))
                }
            )
        }
        composable(Screen.MapView.route) {
            MapViewScreen(onBack = { navController.popBackStack() })
        }
        composable(Screen.SelectLocation.route) {
            SelectLocationScreen(
                onLocationSelected = { lat, lng ->
                    navController.previousBackStackEntry?.savedStateHandle?.set("lat", lat)
                    navController.previousBackStackEntry?.savedStateHandle?.set("lng", lng)
                    navController.popBackStack()
                },
                onBack = { navController.popBackStack() }
            )
        }
        composable(
            route = Screen.StudentDetail.route,
            arguments = listOf(navArgument("studentId") { type = NavType.IntType })
        ) { backStackEntry ->
            val studentId = backStackEntry.arguments?.getInt("studentId") ?: -1
            StudentDetailScreen(studentId = studentId, onBack = { navController.popBackStack() })
        }
    }
}
