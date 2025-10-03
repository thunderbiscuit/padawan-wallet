//
//  LessonScreenNavigation.swift
//  PadawanWallet
//
//  Created by Rubens Machion on 03/10/25.
//

enum LessonScreenNavigation: Hashable, Identifiable {
    var id: Self { self }
    
    case lessonDetails(lesson: LessonItemList)
    case alert(data: BottomSheetView.Data)
}
