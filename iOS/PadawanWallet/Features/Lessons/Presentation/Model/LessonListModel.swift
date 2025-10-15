//
//  LessonListModel.swift
//  PadawanWallet
//
//  Created by Rubens Machion on 05/10/25.
//

import BitcoinUI
import Foundation
import SwiftUI

enum LessonContentItem: Hashable, Identifiable {
    var id: UUID { UUID() }
    case text(key: String)
    case qrCode(address: String)
    case addressCard(address: String)
    case image(name: String)
}

struct LessonSectionList: Hashable, Equatable, Identifiable {
    let id: UUID = UUID()
    let titleKey: String
    var items: [LessonItemList]
}

struct LessonItemList: Hashable, Equatable, Identifiable {
    let id: String
    let titleKey: String
    let navigationTitleKey: String
    let content: [LessonContent]
    var isDone: Bool = false
    let sort: Int
}

struct LessonContent: Hashable, Equatable, Identifiable {
    let id: UUID = UUID()
    let headerKey: String
    let items: [LessonContentItem]
}

extension Text {
    static func lessonText(_ text: String) -> Text {
        return Text(text)
            .font(Fonts.font(.regular, 16))
    }
}

// MARK: - Build all items
extension LessonSectionList {
    static var all: [LessonSectionList] = [
        .init(titleKey: "getting_started", items: LessonItemList.itemsL1),
        .init(titleKey: "transactions", items: LessonItemList.itemsL2),
        .init(titleKey: "wallets", items: LessonItemList.itemsL3),
    ]
}

extension LessonItemList {
    static var itemsL1: [LessonItemList] = [
        .init(id: "l1_title", titleKey: "l1_title", navigationTitleKey: "l1_app_bar", content: LessonContent.contentL1, sort: 1),
        .init(id: "l2_title", titleKey: "l2_title", navigationTitleKey: "l2_app_bar", content: LessonContent.contentL2, sort: 2),
        .init(id: "l3_title", titleKey: "l3_title", navigationTitleKey: "l3_app_bar", content: LessonContent.contentL3, sort: 3),
    ]
    
    static var itemsL2: [LessonItemList] = [
        .init(id: "l4_title", titleKey: "l4_title", navigationTitleKey: "l4_app_bar", content: LessonContent.contentL4, sort: 4),
        .init(id: "l5_title", titleKey: "l5_title", navigationTitleKey: "l5_app_bar", content: LessonContent.contentL5, sort: 5),
        .init(id: "l6_title", titleKey: "l6_title", navigationTitleKey: "l6_app_bar", content: LessonContent.contentL6, sort: 6),
    ]
    
    static var itemsL3: [LessonItemList] = [
        .init(id: "l7_title", titleKey: "l7_title", navigationTitleKey: "l7_app_bar", content: LessonContent.contentL7, sort: 7),
        .init(id: "l8_title", titleKey: "l8_title", navigationTitleKey: "l8_app_bar", content: LessonContent.contentL8, sort: 8),
        .init(id: "l9_title", titleKey: "l9_title", navigationTitleKey: "l9_app_bar", content: LessonContent.contentL9, sort: 9),
    ]
}

// MARK: - Lesson Content Data
extension LessonContent {
    static var contentL1: [LessonContent] = [
        .init(
            headerKey: "l1_title",
            items: [
                .text(key: "l1_p1"),
                .text(key: "l1_p2")
            ]
        ),
        .init(
            headerKey: "l1_h2",
            items: [
                .text(key: "l1_p3"),
                .text(key: "l1_p4")
            ]
        ),
        .init(
            headerKey: "l1_h3",
            items: [
                .text(key: "l1_p5"),
                .text(key: "l1_p6")
            ]
        ),
        .init(
            headerKey: "l1_h4",
            items: [
                .text(key: "l1_p7"),
                .text(key: "l1_p8"),
                .text(key: "l1_p9")
            ]
        ),
    ]
    
    static var contentL2: [LessonContent] = [
        .init(
            headerKey: "l2_title",
            items: [
                .text(key: "l2_p1"),
                .text(key: "l2_p2"),
                .text(key: "l2_p3"),
                .addressCard(address: "tb1pd8jmenqpe7rz2mavfdx7uc8pj7vskxv4rl6avxlqsw2u8u7d4gfs97durt"),
                .text(key: "l2_p4"),
                .qrCode(address: "tb1pd8jmenqpe7rz2mavfdx7uc8pj7vskxv4rl6avxlqsw2u8u7d4gfs97durt"),
                .text(key: "l2_p5"),
                .text(key: "l2_p6"),
            ]
        ),
        .init(
            headerKey: "l2_h1",
            items: [
                .text(key: "l2_p7"),
                .text(key: "l2_p8"),
                .text(key: "l2_p9"),
                .text(key: "l2_p10")
            ]
        ),
        .init(
            headerKey: "l2_h2",
            items: [
                .text(key: "l2_p11"),
                .text(key: "l2_p12"),
                .text(key: "l2_p13")
            ]
        ),
    ]
    
    static var contentL3: [LessonContent] = [
        .init(
            headerKey: "l3_title",
            items: [
                .text(key: "l3_p1"),
                .text(key: "l3_p2")
            ]
        )
    ]
    
    static var contentL4: [LessonContent] = [
        .init(
            headerKey: "l4_title",
            items: [
                .text(key: "l4_p1"),
                .text(key: "l4_p2")
            ]
        ),
        .init(
            headerKey: "l4_h1",
            items: [
                .text(key: "l4_p3"),
                .text(key: "l4_p4")
            ]
        ),
        .init(
            headerKey: "l4_h2",
            items: [
                .text(key: "l4_p5"),
                .text(key: "l4_p6")
            ]
        ),
    ]
    
    static var contentL5: [LessonContent] = [
        .init(
            headerKey: "l5_title",
            items: [
                .text(key: "l5_p1"),
                .text(key: "l5_p2")
            ]
        ),
        .init(
            headerKey: "l5_h1",
            items: [
                .text(key: "l5_p3"),
                .text(key: "l5_p4")
            ]
        ),
        .init(
            headerKey: "l5_h2",
            items: [
                .text(key: "l5_p5")
            ]
        ),
    ]
    
    static var contentL6: [LessonContent] = [
        .init(
            headerKey: "l6_title",
            items: [
                .text(key: "l6_p1")
            ]
        ),
        .init(
            headerKey: "l6_h1",
            items: [
                .text(key: "l6_p3"),
                .text(key: "l6_p4"),
                .image(name: "unit_symbols")
            ]
        ),
        .init(
            headerKey: "l6_h2",
            items: [
                .text(key: "l6_p5"),
                .text(key: "l6_p6"),
                .image(name: "bitcoin_symbols"),
                .text(key: "l6_p7"),
                .image(name: "sat_symbols"),
            ]
        ),
        .init(
            headerKey: "l6_h3",
            items: [
                .text(key: "l6_p8"),
                .text(key: "l6_p9"),
                .text(key: "l6_p10"),
                .text(key: "l6_p11")
            ]
        ),
    ]
    
    static var contentL7: [LessonContent] = [
        .init(
            headerKey: "l7_title",
            items: [
                .text(key: "l7_p1"),
                .text(key: "l7_p2"),
                .text(key: "l7_p3"),
                .text(key: "l7_p4")
            ]
        ),
        .init(
            headerKey: "l7_h1",
            items: [
                .text(key: "l7_p5"),
                .text(key: "l7_p6"),
                .text(key: "l7_p7")
            ]
        ),
    ]
    
    static var contentL8: [LessonContent] = [
        .init(
            headerKey: "l8_title",
            items: [
                .text(key: "l8_p1"),
                .text(key: "l8_p2"),
                .text(key: "l8_p3"),
                .text(key: "l8_p4"),
                .text(key: "l8_p5")
            ]
        ),
        .init(
            headerKey: "l8_h1",
            items: [
                .text(key: "l8_p6"),
                .text(key: "l8_p7"),
                .text(key: "l8_p8")
            ]
        ),
    ]
    
    static var contentL9: [LessonContent] = [
        .init(
            headerKey: "l9_h1",
            items: [
                .text(key: "l9_p1"),
                .text(key: "l9_p2")
            ]
        ),
        .init(
            headerKey: "l9_h2",
            items: [
                .text(key: "l9_p3"),
                .text(key: "l9_p4"),
                .text(key: "l9_p5")
            ]
        ),
        .init(
            headerKey: "l9_h3",
            items: [
                .text(key: "l9_p6"),
                .text(key: "l9_p7"),
                .text(key: "l9_p8"),
                .text(key: "l9_p9"),
                .text(key: "l9_p10"),
                .text(key: "l9_p11")
            ]
        ),
    ]
}
