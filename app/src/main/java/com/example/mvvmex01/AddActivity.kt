package com.example.mvvmex01

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.ViewModelProviders
import com.example.mvvmex01.viewmodels.ContactViewModel
import kotlinx.android.synthetic.main.activity_add.*

/*
1) intent extra로 사용할 상수를 만든다. (companion object)
2) ViewModel 객체를 만든다.
3) 만약 intent가 null이 아니고, extra에 주소록 정보가 모두 들어있다면 EditText와 id값을 지정해준다. MainActivity에서 ADD 버튼을 눌렀을 때에는 신규 추가이므로 인텐트가 없고, RecyclerView item 을 눌렀을 때에는 편집을 할 때에는 해당하는 정보를 불러오기 위해 인텐트 값을 불러올 것이다.
4) 하단의 DONE 버튼을 통해 EditText의 null 체크를 한 후, ViewModel을 통해 insert 해주고, MainActivity로 돌아간다.
 */
class AddActivity : AppCompatActivity() {

    private lateinit var contactViewModel: ContactViewModel
    private var id: Long? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add)

        contactViewModel = ViewModelProviders.of(this).get(ContactViewModel::class.java)

        // intent null check & get extras
        if (intent != null && intent.hasExtra(EXTRA_CONTACT_NAME) && intent.hasExtra(EXTRA_CONTACT_NUMBER)
            && intent.hasExtra(EXTRA_CONTACT_ID)) {
            add_edittext_name.setText(intent.getStringExtra(EXTRA_CONTACT_NAME))
            add_edittext_number.setText(intent.getStringExtra(EXTRA_CONTACT_NUMBER))
            id = intent.getLongExtra(EXTRA_CONTACT_ID, -1)
        }

        add_button.setOnClickListener {
            val name = add_edittext_name.text.toString().trim()
            val number = add_edittext_number.text.toString()

            if (name.isEmpty() || number.isEmpty()) {
                Toast.makeText(this, "Please enter name and number.", Toast.LENGTH_SHORT).show()
            } else {
                val initial = name[0].toUpperCase()
                val contact = Contact(id, name, number, initial)
                contactViewModel.insert(contact)
                finish()
            }
        }
    }

    companion object {
        const val EXTRA_CONTACT_NAME = "EXTRA_CONTACT_NAME"
        const val EXTRA_CONTACT_NUMBER = "EXTRA_CONTACT_NUMBER"
        const val EXTRA_CONTACT_ID = "EXTRA_CONTACT_ID"
    }
}

