package com.qubit.core_vhal.di

import android.content.Context
import com.qubit.core_vhal.VhalReader
import com.qubit.core_vhal.VhalReaderImpl
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
        return VhalReaderImpl(context)
    }
}