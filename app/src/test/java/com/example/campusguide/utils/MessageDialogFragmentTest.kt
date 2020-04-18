package com.example.campusguide.utils

import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.whenever
import java.lang.IllegalStateException
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.ArgumentMatchers.anyString

@RunWith(JUnit4::class)
class MessageDialogFragmentTest {
    @Test(expected = IllegalStateException::class)
    fun testActivityNull() {
        val dialog = MessageDialogFragment("someMessage")
        val manager: FragmentManager = mock()
        val transaction: FragmentTransaction = mock()
        whenever(transaction.add(any(), anyString())).thenReturn(transaction)
        whenever(transaction.commit()).thenReturn(-1)

        whenever(manager.beginTransaction()).thenReturn(transaction)
        dialog.show(manager, "someTag")
        dialog.onCreateDialog(null)
    }
}
