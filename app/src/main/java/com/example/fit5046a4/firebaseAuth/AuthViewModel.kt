package com.example.fit5046a4.firebaseAuth

import androidx.lifecycle.ViewModel
import com.example.fit5046a4.database.UserModel
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.firestore.firestore

class AuthViewModel : ViewModel(){
    private val auth = Firebase.auth // Firebase authentication instance

    private val firestore = Firebase.firestore // Firestore database instance

    /**
     * Logs in a user with email and password.
     * @param email User's email
     * @param password User's password
     * @param OnResult Callback to return success status and error message (if any)
     */
    fun login(email: String, password: String, OnResult: (Boolean, String?)-> Unit){
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener {
                if(it.isSuccessful){
                    OnResult(true,null)
                }else{
                    OnResult(false, it.exception?.localizedMessage)
                }
            }
    }

    /**
     * Registers a new user and stores their info in Firestore.
     * @param email User's email
     * @param password User's password
     * @param OnResult Callback to return success status and error message (if any)
     */
    fun register(email: String, password: String, OnResult: (Boolean, String?)-> Unit){
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener {
                if(it.isSuccessful){
                    var userID = it.result?.user?.uid

                    val userModel = UserModel(email, userID!!)
                    firestore.collection("users").document(userID)
                        .set(userModel)
                        .addOnCompleteListener { dbTask->
                            if(dbTask.isSuccessful){
                                OnResult(true,null)
                            }else{
                                OnResult(false, "Something went wrong")
                            }
                        }
                }else{
                    OnResult(false, it.exception?.localizedMessage)
                }
            }
    }

    /**
     * Registers a new user and stores their info in Firestore.
     * @param email User's email
     * @param password User's password
     * @param OnResult Callback to return success status and error message (if any)
     */
    fun resetPassword(email: String, callback: (Boolean, String?) -> Unit) {
        FirebaseAuth.getInstance().sendPasswordResetEmail(email)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    callback(true, null)
                } else {
                    callback(false, task.exception?.message)
                }
            }
    }

}