package com.example.mvvmex01

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mvvmex01.adapters.ContactAdapter
import com.example.mvvmex01.viewmodels.ContactViewModel
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    private lateinit var contactViewModel: ContactViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        // Set contactItemClick & contactItemLongClick lambda
        /*
        Adapter에 onClick 시에 해야할 일과 onLongClick 시에 해야할 일 두 개의 (Contact) -> Unit파라미터를 넘겨주어야 한다.
        클릭했을 때에는 현재 contact에서 name, number, id를 뽑아 인텐트에 포함시켜 AddActivity로 넘겨주면서 액티비티를 시작하도록 만들 것이다.
        AddActivity를 우선 만든 후에 수정해 주기로 한다.
        또, 롱클릭 했을 때에는 다이얼로그를 통해 아이템을 삭제하도록 만들었다.
         */
        val adapter = ContactAdapter({ contact ->
            // put extras of contact info & start AddActivity
            val intent = Intent(this, AddActivity::class.java)
            intent.putExtra(AddActivity.EXTRA_CONTACT_NAME, contact.name)
            intent.putExtra(AddActivity.EXTRA_CONTACT_NUMBER, contact.number)
            intent.putExtra(AddActivity.EXTRA_CONTACT_ID, contact.id)
            startActivity(intent)
        }, { contact ->
            deleteDialog(contact)
        })

        val lm = LinearLayoutManager(this)

        main_recycleview.adapter = adapter
        main_recycleview.layoutManager = lm
        main_recycleview.setHasFixedSize(true)

        /*
        뷰모델 객체는 직접적으로 초기화 해주는 것이 아니라, 안드로이드 시스템을 통해 생성해준다.
         시스템에서는 만약 이미 생성된 ViewModel 인스턴스가 있다면 이를 반환할 것이므로 메모리 낭비를 줄여준다.
         따라서 ViewModelProviders를 이용해 get 해준다.
         */
        contactViewModel = ViewModelProviders.of(this).get(ContactViewModel::class.java)
        /*
        또한 Observere를 만들어서 뷰모델이 어느 액티비티/프래그먼트의 생명주기를 관찰할 것인지 정한다.
        이 액티비티가 파괴되면 시점에 시스템에서 뷰모델도 자동으로 파괴할 것이다.
         Kotlin 에서는 람다를 이용해 보다 간편하게 사용할 수 있다.
         */
        contactViewModel.getAll().observe(this, Observer<List<Contact>> { contacts ->
            // Update UI
            // Observer는 onChanged 메소드를 가지고 있다.
            // 즉, 관찰하고 있던 LiveData가 변하면 무엇을 할 것인지 액션을 지정할 수 있다.
            // 이후 액티비티/프래그먼트가 활성화되어 있다면 View에서 LiveData를 관찰하여
            // 자동으로 변경 사항을 파악하고 이를 수행한다.
            // 이 부분에서 UI를 업데이트 하도록 만들 것이다.
            adapter.setContacts(contacts!!)
        })

        main_button.setOnClickListener {
            val intent = Intent(this, AddActivity::class.java)
            startActivity(intent)
        }
    }
    private fun deleteDialog(contact: Contact) {
        val builder = AlertDialog.Builder(this)
        builder.setMessage("Delete selected contact?")
            .setNegativeButton("NO") { _, _ -> }
            .setPositiveButton("YES") { _, _ ->
                contactViewModel.delete(contact)
            }
        builder.show()
    }
}
