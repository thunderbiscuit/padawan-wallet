//
//  LessonListModel.swift
//  PadawanWallet
//
//  Created by Rubens Machion on 05/10/25.
//

import BitcoinUI
import Foundation
import SwiftUI

struct LessonSectionList: Hashable, Equatable, Identifiable {
    let id: UUID = UUID()
    let title: String
    var items: [LessonItemList]
}

struct LessonItemList: Hashable, Equatable, Identifiable {
    let id: String
    let title: String
    let navigationTitle: String
    let content: [LessonContent]
    var isDone: Bool = false
    let sort: Int
}

struct LessonContent: Hashable, Equatable, Identifiable {
    let id: UUID = UUID()
    let header: String
    let texts: [AnyHashableView]
}

struct AnyHashableView: Hashable, Equatable, Identifiable {
    let id: UUID
    let view: AnyView

    init<V: View>(_ view: V, id: UUID = UUID()) {
        self.id = id
        self.view = AnyView(view)
    }

    static func == (lhs: AnyHashableView, rhs: AnyHashableView) -> Bool {
        lhs.id == rhs.id
    }

    func hash(into hasher: inout Hasher) {
        hasher.combine(id)
    }
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
        .init(
            title: Strings.gettingStarted,
            items: LessonItemList.itemsL1
        ),
        .init(
            title: Strings.transactions,
            items: LessonItemList.itemsL2
        ),
        .init(
            title: Strings.wallets,
            items: LessonItemList.itemsL3
        ),
    ]
}

extension LessonItemList {
    static var itemsL1: [LessonItemList] = [
        .init(
            id: "l1_title",
            title: Strings.l1Title,
            navigationTitle: Strings.l1AppBar,
            content: LessonContent.contentL1,
            sort: 1
        ),
        .init(
            id: "l2_title",
            title: Strings.l2Title,
            navigationTitle: Strings.l2AppBar,
            content: LessonContent.contentL2,
            sort: 2
        ),
        .init(
            id: "l3_title",
            title: Strings.l3Title,
            navigationTitle: Strings.l3AppBar,
            content: LessonContent.contentL3,
            sort: 3
        ),
    ]
    
    static var itemsL2: [LessonItemList] = [
        .init(
            id: "l4_title",
            title: Strings.l4Title,
            navigationTitle: Strings.l4AppBar,
            content: LessonContent.contentL4,
            sort: 4
        ),
        .init(
            id: "l5_title",
            title: Strings.l5Title,
            navigationTitle: Strings.l5AppBar,
            content: LessonContent.contentL5,
            sort: 5
        ),
        .init(
            id: "l6_title",
            title: Strings.l6Title,
            navigationTitle: Strings.l6AppBar,
            content: LessonContent.contentL6,
            sort: 6
        ),
    ]
    
    static var itemsL3: [LessonItemList] = [
        .init(
            id: "l7_title",
            title: Strings.l7Title,
            navigationTitle: Strings.l7AppBar,
            content: LessonContent.contentL7,
            sort: 7
        ),
        .init(
            id: "l8_title",
            title: Strings.l8Title,
            navigationTitle: Strings.l8AppBar,
            content: LessonContent.contentL8,
            sort: 8
        ),
        .init(
            id: "l9_title",
            title: Strings.l9Title,
            navigationTitle: Strings.l9AppBar,
            content: LessonContent.contentL9,
            sort: 9
        ),
    ]
}

extension LessonContent {
    static var contentL1: [LessonContent] = [
        .init(
            header: Strings.l1Title,
            texts: [
                AnyHashableView(Text.lessonText(Strings.l1P1)),
                AnyHashableView(Text.lessonText(Strings.l1P2))
            ]
        ),
        .init(
            header: Strings.l1H2,
            texts: [
                AnyHashableView(Text.lessonText(Strings.l1P3)),
                AnyHashableView(Text.lessonText(Strings.l1P4)),
            ]
        ),
        .init(
            header: Strings.l1H3,
            texts: [
                AnyHashableView(Text.lessonText(Strings.l1P5)),
                AnyHashableView(Text.lessonText(Strings.l1P6)),
            ]
        ),
        .init(
            header: Strings.l1H4,
            texts: [
                AnyHashableView(Text.lessonText(Strings.l1P7)),
                AnyHashableView(Text.lessonText(Strings.l1P8)),
                AnyHashableView(Text.lessonText(Strings.l1P9)),
            ]
        )
    ]
    
    static var contentL2: [LessonContent] = [
        .init(
            header: Strings.l2Title,
            texts: [
                AnyHashableView(Text.lessonText(Strings.l2P1)),
                AnyHashableView(Text.lessonText(Strings.l2P2)),
                AnyHashableView(Text.lessonText(Strings.l2P3)),
                AnyHashableView(
                    PadawanCardView(
                        backgroundColor: PadawanColorTheme.tatooine.colors.background2,
                        content: {
                            Text("tb1pd8jmenqpe7rz2mavfdx7uc8pj7vskxv4rl6avxlqsw2u8u7d4gfs97durt")
                                .font(Fonts.font(.regular, 13))
                                .padding(.horizontal, 16)
                        }
                    )
                    .padding(.horizontal, 8)
                    .frame(height: 120)
                ),
                AnyHashableView(Text.lessonText(Strings.l2P4)),
                AnyHashableView(
                    PadawanCardView(
                        backgroundColor: PadawanColorTheme.tatooine.colors.background2,
                        content: {
                            QRCodeView(qrCodeType: .bitcoin("tb1pd8jmenqpe7rz2mavfdx7uc8pj7vskxv4rl6avxlqsw2u8u7d4gfs97durt"))
                        }
                    )
                    .padding(.horizontal, 8)
                    .frame(height: 120)
                ),
                AnyHashableView(Text.lessonText(Strings.l2P5)),
                AnyHashableView(Text.lessonText(Strings.l2P6)),
            ]
        ),
        .init(
            header: Strings.l2H1,
            texts: [
                AnyHashableView(Text.lessonText(Strings.l2P7)),
                AnyHashableView(Text.lessonText(Strings.l2P8)),
                AnyHashableView(Text.lessonText(Strings.l2P9)),
                AnyHashableView(Text.lessonText(Strings.l2P10)),
            ]
        ),
        .init(
            header: Strings.l2H2,
            texts: [
                AnyHashableView(Text.lessonText(Strings.l2P11)),
                AnyHashableView(Text.lessonText(Strings.l2P12)),
                AnyHashableView(Text.lessonText(Strings.l2P13)),
            ]
        )
    ]
    
    static var contentL3: [LessonContent] = [
        .init(
            header: Strings.l3Title,
            texts: [
                AnyHashableView(Text.lessonText(Strings.l3P1)),
                AnyHashableView(Text.lessonText(Strings.l3P2)),
            ]
        )
    ]
    
    static var contentL4: [LessonContent] = [
        .init(
            header: Strings.l4Title,
            texts: [
                AnyHashableView(Text.lessonText(Strings.l4P1)),
                AnyHashableView(Text.lessonText(Strings.l4P2)),
            ]
        ),
        .init(
            header: Strings.l4H1,
            texts: [
                AnyHashableView(Text.lessonText(Strings.l4P3)),
                AnyHashableView(Text.lessonText(Strings.l4P4)),
            ]
        ),
        .init(
            header: Strings.l4H2,
            texts: [
                AnyHashableView(Text.lessonText(Strings.l4P5)),
                AnyHashableView(Text.lessonText(Strings.l4P6)),
            ]
        )
    ]
    
    static var contentL5: [LessonContent] = [
        .init(
            header: Strings.l5Title,
            texts: [
                AnyHashableView(Text.lessonText(Strings.l5P1)),
                AnyHashableView(Text.lessonText(Strings.l5P2)),
            ]
        ),
        .init(
            header: Strings.l5H1,
            texts: [
                AnyHashableView(Text.lessonText(Strings.l5P3)),
                AnyHashableView(Text.lessonText(Strings.l5P4)),
            ]
        ),
        .init(
            header: Strings.l5H2,
            texts: [
                AnyHashableView(Text.lessonText(Strings.l5P5)),
            ]
        )
    ]
    
    static var contentL6: [LessonContent] = [
        .init(
            header: Strings.l6Title,
            texts: [
                AnyHashableView(Text.lessonText(Strings.l6P1)),
            ]
        ),
        .init(
            header: Strings.l6H1,
            texts: [
                AnyHashableView(Text.lessonText(Strings.l6P3)),
                AnyHashableView(Text.lessonText(Strings.l6P4)),
                AnyHashableView(
                    PadawanCardView(
                        backgroundColor: PadawanColorTheme.tatooine.colors.background2,
                        content: {
                            Asset.Images.unitSymbols.toImage
                                .resizable()
                                .aspectRatio(contentMode: .fit)
                        }
                    )
                    .padding(.horizontal, 8)
                    .frame(height: 120)
                )
            ]
        ),
        .init(
            header: Strings.l6H2,
            texts: [
                AnyHashableView(Text.lessonText(Strings.l6P5)),
                AnyHashableView(Text.lessonText(Strings.l6P6)),
                AnyHashableView(
                    PadawanCardView(
                        backgroundColor: PadawanColorTheme.tatooine.colors.background2,
                        content: {
                            Asset.Images.bitcoinSymbols.toImage
                                .resizable()
                        }
                    )
                    .padding(.horizontal, 8)
                    .frame(height: 120)
                ),
                AnyHashableView(Text.lessonText(Strings.l6P7)),
                AnyHashableView(
                    PadawanCardView(
                        backgroundColor: PadawanColorTheme.tatooine.colors.background2,
                        content: {
                            Asset.Images.satSymbols.toImage
                                .resizable()
                        }
                    )
                    .padding(.horizontal, 8)
                    .frame(height: 120)
                )
            ]
        ),
        .init(
            header: Strings.l6H3,
            texts: [
                AnyHashableView(Text.lessonText(Strings.l6P8)),
                AnyHashableView(Text.lessonText(Strings.l6P9)),
                AnyHashableView(Text.lessonText(Strings.l6P10)),
                AnyHashableView(Text.lessonText(Strings.l6P11)),
            ]
        )
    ]
    
    static var contentL7: [LessonContent] = [
        .init(
            header: Strings.l7Title,
            texts: [
                AnyHashableView(Text.lessonText(Strings.l7P1)),
                AnyHashableView(Text.lessonText(Strings.l7P2)),
                AnyHashableView(Text.lessonText(Strings.l7P3)),
                AnyHashableView(Text.lessonText(Strings.l7P4)),
            ]
        ),
        .init(
            header: Strings.l7H1,
            texts: [
                AnyHashableView(Text.lessonText(Strings.l7P5)),
                AnyHashableView(Text.lessonText(Strings.l7P6)),
                AnyHashableView(Text.lessonText(Strings.l7P7)),
            ]
        )
    ]
    
    static var contentL8: [LessonContent] = [
        .init(
            header: Strings.l8Title,
            texts: [
                AnyHashableView(Text.lessonText(Strings.l8P1)),
                AnyHashableView(Text.lessonText(Strings.l8P2)),
                AnyHashableView(Text.lessonText(Strings.l8P3)),
                AnyHashableView(Text.lessonText(Strings.l8P4)),
                AnyHashableView(Text.lessonText(Strings.l8P5)),
            ]
        ),
        .init(
            header: Strings.l8H1,
            texts: [
                AnyHashableView(Text.lessonText(Strings.l8P6)),
                AnyHashableView(Text.lessonText(Strings.l8P7)),
                AnyHashableView(Text.lessonText(Strings.l8P8)),
            ]
        ),
    ]
    
    static var contentL9: [LessonContent] = [
        .init(
            header: Strings.l9H1,
            texts: [
                AnyHashableView(Text.lessonText(Strings.l9P1)),
                AnyHashableView(Text.lessonText(Strings.l9P2)),
            ]
        ),
        .init(
            header: Strings.l9H2,
            texts: [
                AnyHashableView(Text.lessonText(Strings.l9P3)),
                AnyHashableView(Text.lessonText(Strings.l9P4)),
                AnyHashableView(Text.lessonText(Strings.l9P5)),
            ]
        ),
        .init(
            header: Strings.l9H3,
            texts: [
                AnyHashableView(Text.lessonText(Strings.l9P6)),
                AnyHashableView(Text.lessonText(Strings.l9P7)),
                AnyHashableView(Text.lessonText(Strings.l9P8)),
                AnyHashableView(Text.lessonText(Strings.l9P9)),
                AnyHashableView(Text.lessonText(Strings.l9P10)),
                AnyHashableView(Text.lessonText(Strings.l9P11)),
            ]
        ),
    ]
}
