package com.example.myapplication

import android.content.Context
import android.content.pm.PackageManager
import android.os.Bundle
import android.provider.ContactsContract
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.io.Serializable
import java.util.*

const val EXTRA_MESSAGE = "com.example.myfirstapp.MESSAGE"

class MainActivity : AppCompatActivity() {

    init{
        instance = this
    }

    class ContactItem : Serializable {
        var user_phNumber: String? = null
        var user_Name: String? = null
        //사진 부분
//    var photo_id: Long = 0
//    var person_id: Long = 0
        var id: Int = 0
        val phNumberChanged: String
            get() = user_phNumber!!.replace("-", "")

        //실제 연락처를 ArrayList 형태로 가져오는 함수
        //            if (contactItem.getUser_phNumber().startsWith("01")) {
        //                hashList.add(contactItem);
        //                //contactsList.add(myContact);
        //                Log.d("<<CONTACTS>>", "name=" + contactItem.getUser_Name() + ", phone=" + contactItem.getUser_phNumber());
        //            }

        override fun toString(): String {
            return this.user_phNumber!!
        }

        override fun hashCode(): Int {
            return phNumberChanged.hashCode()
        }

        fun equals(o: Serializable?): Boolean {
            return if (o is ContactItem) phNumberChanged == o.phNumberChanged else false

        }

    }
    val contactList: ArrayList<ContactItem>
        get() {
            val uri = ContactsContract.CommonDataKinds.Phone.CONTENT_URI
            val projection = arrayOf(ContactsContract.CommonDataKinds.Phone.NUMBER, ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME, ContactsContract.Contacts.PHOTO_ID, ContactsContract.Contacts._ID)

            val selectionArgs: Array<String>? = null
            val sortOrder = ContactsContract.Contacts.DISPLAY_NAME + " COLLATE LOCALIZED ASC"

//            val contentResolver = Application().applicationContext.contentResolver
//            val cursor = contentResolver.query(uri, projection, null,
//                selectionArgs, sortOrder)
            val cursor = context().contentResolver.query(uri, projection, null,
                selectionArgs, sortOrder)



            val hashlist = LinkedHashSet<ContactItem>()
            val contactItems: ArrayList<ContactItem>
            if (cursor!!.moveToFirst()) {
                do {
//                    val photo_id = cursor!!.getLong(2)
//                    val person_id = cursor!!.getLong(3)

                    val contactItem = ContactItem()
                    contactItem.user_phNumber = cursor.getString(0)
                    contactItem.user_Name = cursor.getString(1)
//                    contactItem.photo_id = photo_id
//                    contactItem.person_id = person_id

                    hashlist.add(contactItem)

                } while (cursor.moveToNext())
            }

            contactItems = ArrayList(hashlist)
            for (i in contactItems.indices) {
                contactItems[i].id = i
            }

            cursor.close()


            return contactItems
        }

//    class App : Application() {
//        init {
//            instance = this
//        }
//    }
    private var mArrayList: ArrayList<ContactItem>? = null
//    private var mArrayList: ArrayList<Dictionary>? = null
    private var mAdapter: CustomAdapter? = null
    private var count = -1
//    private val contactlist = contactList

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        //권한이 부여되어 있는지 확인
        val permissionCheck = ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_CONTACTS)


        //MainActivity 돌아올때마다 메세지 나옴
        if (permissionCheck == PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(applicationContext, "연락처 열람권한 있음", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(applicationContext, "연락처 열람권한 없음", Toast.LENGTH_SHORT).show()

            //권한설정 dialog에서 거부를 누르면
            //ActivityCompat.shouldShowRequestPermissionRationale 메소드의 반환값이 true가 된다.
            //단, 사용자가 "Don't ask again"을 체크한 경우
            //거부하더라도 false를 반환하여, 직접 사용자가 권한을 부여하지 않는 이상, 권한을 요청할 수 없게 된다.
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, android.Manifest.permission.READ_CONTACTS)) {
                //이곳에 권한이 왜 필요한지 설명하는 Toast나 dialog를 띄워준 후, 다시 권한을 요청한다.
                Toast.makeText(applicationContext, "연락처 권한이 필요합니다", Toast.LENGTH_SHORT).show()
                ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.READ_CONTACTS), READ_CONTACTS_PERMISSION)
            } else {
                ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.READ_CONTACTS), READ_CONTACTS_PERMISSION)
            }

        }
        val mRecyclerView = findViewById(R.id.recyclerview_main_list) as RecyclerView
        val mLinearLayoutManager = LinearLayoutManager(this)
        mRecyclerView.layoutManager = mLinearLayoutManager


//        mArrayList = ArrayList()
        mArrayList = contactList


        mAdapter = CustomAdapter(mArrayList)
        mRecyclerView.adapter = mAdapter


        val dividerItemDecoration = DividerItemDecoration(
            mRecyclerView.context,
            mLinearLayoutManager.orientation
        )
        mRecyclerView.addItemDecoration(dividerItemDecoration)
        // 버튼 부분
//        val buttonInsert = findViewById(R.id.button_main_insert) as Button
//        buttonInsert.setOnClickListener {
//            count++
//
//            val data = Dictionary("이름$count", "전화번호$count")
//
//            //mArrayList.add(0, dict); //RecyclerView의 첫 줄에 삽입
//            mArrayList!!.add(data) // RecyclerView의 마지막 줄에 삽입
//
//            mAdapter!!.notifyDataSetChanged()
//        }

    }


    //필요한건지 모르겠으나 grantResults[]는 요청한 권한의 허용 여부 확인 가능
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        when (requestCode) {
            READ_CONTACTS_PERMISSION -> if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(applicationContext, "연락처권한 승인함", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(applicationContext, "연락처권한 거부함", Toast.LENGTH_SHORT).show()
            }
        }
    }

    companion object {
        internal val READ_CONTACTS_PERMISSION = 1
        private var instance: MainActivity? = null

        fun context() : Context {
            return instance!!.applicationContext
        }
    }
    //id 값은 ListView의 postion값
//연락처의 사진을 가져오기 위해선 photo_id, person_id 필요
//사용자 연락처 클래스 정의
    /** Called when the user taps the Send button */
//    fun sendMessage(view: View) {
//        val editText = findViewById<EditText>(R.id.editText)
//        val message = editText.text.toString()
//        val intent = Intent(this, DisplayMessageActivity::class.java).apply {
//            putExtra(EXTRA_MESSAGE, message)
//        }
//        startActivity(intent)
//    }
}

