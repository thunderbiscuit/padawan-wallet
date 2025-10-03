//
//  LessonsListViewModel.swift
//  PadawanWallet
//
//  Created by Rubens Machion on 05/10/25.
//

import SwiftUI
import Foundation

@MainActor
final class LessonsListViewModel: ObservableObject {
    
    @Binding var path: NavigationPath
    
    @Published var list: [LessonSectionList] = []
 
    private let storage: StorageProtocol
    
    init(
        path: Binding<NavigationPath>,
        storage: StorageProtocol = UserDefaultsStorage.shared
    ) {
        _path = path
        self.storage = storage
    }
    
    func showLesson(for lesson: LessonItemList) {
        path.append(LessonScreenNavigation.lessonDetails(lesson: lesson))
    }
    
    func updateList() async {
        var section = LessonSectionList.all
        
        for i in 0..<section.count {
            for j in 0..<section[i].items.count {
                let key = section[i].items[j].id
                section[i].items[j].isDone = storage.get(key) ?? false
            }
        }
        
        self.list = section        
    }
}
