package com.qubit.core_vhal.di

import android.content.Context
import com.qubit.core_vhal.repository.HybridVhalReader
import com.qubit.core_vhal.repository.MockVhalReaderImpl
import com.qubit.core_vhal.repository.VhalReader
import com.qubit.core_vhal.repository.VhalReaderImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object VhalModule {
    @Provides
    @Singleton
    fun provideVhalReader(
        @ApplicationContext context: Context
    ): VhalReader {
        return HybridVhalReader(
            realReader = VhalReaderImpl(context),
            mockReader = MockVhalReaderImpl()
        )
    }
}