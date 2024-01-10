import android.content.Context
import android.widget.Toast
import com.google.firebase.firestore.FirebaseFirestore

class NoteDelete(private val context: Context) {

    private val db: FirebaseFirestore = FirebaseFirestore.getInstance()

    fun deleteNoteFromFirestore(noteId: String?, onSuccess: () -> Unit, onFailure: () -> Unit) {
        if (noteId != null) {
            db.collection("notes").document(noteId)
                .delete()
                .addOnSuccessListener {
                    onSuccess.invoke()
                }
                .addOnFailureListener {
                    onFailure.invoke()
                    Toast.makeText(context, "Failed to Delete", Toast.LENGTH_SHORT).show()
                }
        }
    }
}
