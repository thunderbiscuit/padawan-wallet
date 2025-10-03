//
//  LessonsDetailViewModel.swift
//  PadawanWallet
//
//  Created by Rubens Machion on 05/10/25.
//

import SwiftUI
import Foundation

@MainActor
final class LessonsDetailViewModel: ObservableObject {
    
    @Binding var path: NavigationPath
    @Published var selectedIndex: Int = 0
    
    let lesson: LessonItemList
    
    private let storage: StorageProtocol
    
    var totalPages: Int {
        lesson.content.count
    }
    
    var nextPageImage: Image {
        if selectedIndex < totalPages - 1 {
            return Image(systemName: "chevron.right")
        } else {
            return Image(systemName: "checkmark.circle")
        }
    }
 
    init(
        path: Binding<NavigationPath>,
        lesson: LessonItemList,
        storage: StorageProtocol = UserDefaultsStorage.shared
    ) {
        _path = path
        self.lesson = lesson
        self.storage = storage
    }
    
    func goBack() {
        if selectedIndex > 0 {
            selectedIndex -= 1
        }
    }
    
    func goToNextPage() {
        if selectedIndex < totalPages - 1 {
            selectedIndex += 1
        } else {
            storage.set(true, key: lesson.id)
            path.removeLast()
        }
    }
}
