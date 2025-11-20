package com.xxmrk888ytxx.privatenote.DI

import com.xxmrk888ytxx.privatenote.domain.UseCases.ClearTempDirUseCase.ClearShareDirUseCase
import com.xxmrk888ytxx.privatenote.domain.UseCases.ClearTempDirUseCase.ClearShareDirUseCaseImpl
import com.xxmrk888ytxx.privatenote.domain.UseCases.CreateBackupUseCase.CreateBackupModelUseCase
import com.xxmrk888ytxx.privatenote.domain.UseCases.CreateBackupUseCase.CreateBackupModelUseCaseImpl
import com.xxmrk888ytxx.privatenote.domain.UseCases.ExportAudioUseCase.ExportAudioUseCase
import com.xxmrk888ytxx.privatenote.domain.UseCases.ExportAudioUseCase.ExportAudioUseCaseImpl
import com.xxmrk888ytxx.privatenote.domain.UseCases.ExportImageUseCase.ExportImageUseCase
import com.xxmrk888ytxx.privatenote.domain.UseCases.ExportImageUseCase.ExportImageUseCaseImpl
import com.xxmrk888ytxx.privatenote.domain.UseCases.GetCategoryForBackupUseCase.GetCategoryForBackupUseCase
import com.xxmrk888ytxx.privatenote.domain.UseCases.GetCategoryForBackupUseCase.GetCategoryForBackupUseCaseImpl
import com.xxmrk888ytxx.privatenote.domain.UseCases.GetNotesForBackupUseCase.GetNotesForBackupUseCase
import com.xxmrk888ytxx.privatenote.domain.UseCases.GetNotesForBackupUseCase.GetNotesForBackupUseCaseImpl
import com.xxmrk888ytxx.privatenote.domain.UseCases.GetTodoForBackupUseCase.GetTodoForBackupUseCase
import com.xxmrk888ytxx.privatenote.domain.UseCases.GetTodoForBackupUseCase.GetTodoForBackupUseCaseImpl
import com.xxmrk888ytxx.privatenote.domain.UseCases.OpenImageInGallaryUseCase.OpenImageInGalleryUseCase
import com.xxmrk888ytxx.privatenote.domain.UseCases.OpenImageInGallaryUseCase.OpenImageInGalleryUseCaseImpl
import com.xxmrk888ytxx.privatenote.domain.UseCases.ProvideDataFromFileUriUseCase.ProvideDataFromFileUriUseCase
import com.xxmrk888ytxx.privatenote.domain.UseCases.ProvideDataFromFileUriUseCase.ProvideDataFromFileUriUseCaseImpl
import com.xxmrk888ytxx.privatenote.domain.UseCases.ReadBackupFileUseCase.UnArcherBackupUseCase
import com.xxmrk888ytxx.privatenote.domain.UseCases.ReadBackupFileUseCase.UnArcherBackupUseCaseImpl
import com.xxmrk888ytxx.privatenote.domain.UseCases.RemoveNoteFileUseCase.RemoveNoteFileUseCase
import com.xxmrk888ytxx.privatenote.domain.UseCases.RemoveNoteFileUseCase.RemoveNoteFileUseCaseImpl
import com.xxmrk888ytxx.privatenote.domain.UseCases.RemoveNotifyTaskIfTodoCompletedUseCase.RemoveNotifyTaskIfTodoCompletedUseCase
import com.xxmrk888ytxx.privatenote.domain.UseCases.RemoveNotifyTaskIfTodoCompletedUseCase.RemoveNotifyTaskIfTodoCompletedUseCaseImpl
import com.xxmrk888ytxx.privatenote.domain.UseCases.RestoreBackupUseCase.RestoreBackupUseCase
import com.xxmrk888ytxx.privatenote.domain.UseCases.RestoreBackupUseCase.RestoreBackupUseCaseImpl
import com.xxmrk888ytxx.privatenote.domain.UseCases.RestoreCategoryFromUseCase.RestoreCategoryFromBackupUseCase
import com.xxmrk888ytxx.privatenote.domain.UseCases.RestoreCategoryFromUseCase.RestoreCategoryFromBackupUseCaseImpl
import com.xxmrk888ytxx.privatenote.domain.UseCases.RestoreNoteFromBackupUseCase.RestoreNoteFromBackupUseCase
import com.xxmrk888ytxx.privatenote.domain.UseCases.RestoreNoteFromBackupUseCase.RestoreNoteFromBackupUseCaseImpl
import com.xxmrk888ytxx.privatenote.domain.UseCases.RestoreTodoFromUseCase.RestoreTodoFromUseCase
import com.xxmrk888ytxx.privatenote.domain.UseCases.RestoreTodoFromUseCase.RestoreTodoFromUseCaseImpl
import com.xxmrk888ytxx.privatenote.domain.UseCases.WriteBackupInFileUseCase.WriteBackupInFileUseCase
import com.xxmrk888ytxx.privatenote.domain.UseCases.WriteBackupInFileUseCase.WriteBackupInFileUseCaseImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
interface UseCasesModule {
    @Binds
    fun bindsRemoveNoteFileUseCase(removeNoteFileUseCaseImpl: RemoveNoteFileUseCaseImpl): RemoveNoteFileUseCase

    @Binds
    fun bindsRemoveNotifyTaskIfTodoCompletedUseCase(
        notifyTaskIfTodoCompletedUseCaseImpl: RemoveNotifyTaskIfTodoCompletedUseCaseImpl,
    )
            : RemoveNotifyTaskIfTodoCompletedUseCase

    @Binds
    fun bindsGetNotesForBackupUseCase(getNotesForBackupUseCaseImpl: GetNotesForBackupUseCaseImpl)
            : GetNotesForBackupUseCase

    @Binds
    fun bindsGetCategoryForBackupUseCase(getCategoryForBackupUseCaseImpl: GetCategoryForBackupUseCaseImpl)
            : GetCategoryForBackupUseCase

    @Binds
    fun bindsTodoGetForBackupUseCase(getTodoForBackupUseCaseImpl: GetTodoForBackupUseCaseImpl)
            : GetTodoForBackupUseCase

    @Binds
    fun bindsWriteBackupInFileUseCase(writeBackupInFileUseCaseImpl: WriteBackupInFileUseCaseImpl)
            : WriteBackupInFileUseCase

    @Binds
    fun bindsReadBackupFileUseCase(unArcherBackupUseCaseImpl: UnArcherBackupUseCaseImpl)
            : UnArcherBackupUseCase

    @Binds
    fun bindsRestoreCategoryFromUseCase(restoreCategoryFromBackupUseCase: RestoreCategoryFromBackupUseCaseImpl)
            : RestoreCategoryFromBackupUseCase

    @Binds
    fun bindsRestoreNoteFromUseCase(restoreNoteFromBackupUseCaseImpl: RestoreNoteFromBackupUseCaseImpl)
            : RestoreNoteFromBackupUseCase

    @Binds
    fun bindsRestoreTodoFromBackupUseCase(restoreTodoFromUseCaseImpl: RestoreTodoFromUseCaseImpl)
            : RestoreTodoFromUseCase

    @Binds
    fun bindsCreateBackupUseCase(createBackupModelUseCaseImpl: CreateBackupModelUseCaseImpl): CreateBackupModelUseCase

    @Binds
    fun bindsExportImageUseCase(exportImageUseCaseImpl: ExportImageUseCaseImpl): ExportImageUseCase

    @Binds
    fun bindsExportAudioUseCase(exportAudioUseCaseImpl: ExportAudioUseCaseImpl): ExportAudioUseCase

    @Binds
    fun bindsRestoreBackupUseCase(restoreBackupUseCaseImpl: RestoreBackupUseCaseImpl): RestoreBackupUseCase

    @Binds
    fun bindProvideDataFromFileUriUseCase(
        ProvideDataFromFileUriUseCaseImpl: ProvideDataFromFileUriUseCaseImpl
    ): ProvideDataFromFileUriUseCase

    @Binds
    fun bindOpenImageInGalleryUseCase(
        openImageInGalleryUseCase:OpenImageInGalleryUseCaseImpl
    ) : OpenImageInGalleryUseCase

    @Binds
    fun bindClearShareDirUseCase(
        clearShareDirUseCase: ClearShareDirUseCaseImpl
    ) : ClearShareDirUseCase
}