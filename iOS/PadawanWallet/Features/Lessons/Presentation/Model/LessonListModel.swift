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
    let headerKey: String
    let texts: [AnyHashableView]
}

struct AnyHashableView: Hashable, Equatable, Identifiable {
    let id: UUID = UUID()
    let view: AnyView

    init<V: View>(_ view: V, id: UUID = UUID()) {
        self.view = AnyView(view)
    }

    static func == (lhs: AnyHashableView, rhs: AnyHashableView) -> Bool {
        lhs.id == rhs.id
    }

    func hash(into hasher: inout Hasher) {
        hasher.combine(id)
    }
}

struct LocalizedLessonText: View {
    @ObservedObject private var languageManager = LanguageManager.shared
    let localizationKey: String
    init(_ key: String) {
        self.localizationKey = key
    }
    var body: some View {
        Text.lessonText(languageManager.localizedString(forKey: localizationKey))
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
    static var all: [LessonSectionList] { [
        .init(
            title: LanguageManager.shared.localizedString(forKey: "getting_started"),
            items: LessonItemList.itemsL1
        ),
        .init(
            title: LanguageManager.shared.localizedString(forKey: "transactions"),
            items: LessonItemList.itemsL2
        ),
        .init(
            title: LanguageManager.shared.localizedString(forKey: "wallets"),
            items: LessonItemList.itemsL3
        ),
    ] }
}

extension LessonItemList {
    static var itemsL1: [LessonItemList] { [
        .init(
            id: "l1_title",
            title: LanguageManager.shared.localizedString(forKey: "l1_title"),
            navigationTitle: LanguageManager.shared.localizedString(forKey: "l1_app_bar"),
            content: LessonContent.contentL1,
            sort: 1
        ),
        .init(
            id: "l2_title",
            title: LanguageManager.shared.localizedString(forKey: "l2_title"),
            navigationTitle: LanguageManager.shared.localizedString(forKey: "l2_app_bar"),
            content: LessonContent.contentL2,
            sort: 2
        ),
        .init(
            id: "l3_title",
            title: LanguageManager.shared.localizedString(forKey: "l3_title"),
            navigationTitle: LanguageManager.shared.localizedString(forKey: "l3_app_bar"),
            content: LessonContent.contentL3,
            sort: 3
        ),
    ] }
    
    static var itemsL2: [LessonItemList] { [
        .init(
            id: "l4_title",
            title: LanguageManager.shared.localizedString(forKey: "l4_title"),
            navigationTitle: LanguageManager.shared.localizedString(forKey: "l4_app_bar"),
            content: LessonContent.contentL4,
            sort: 4
        ),
        .init(
            id: "l5_title",
            title: LanguageManager.shared.localizedString(forKey: "l5_title"),
            navigationTitle: LanguageManager.shared.localizedString(forKey: "l5_app_bar"),
            content: LessonContent.contentL5,
            sort: 5
        ),
        .init(
            id: "l6_title",
            title: LanguageManager.shared.localizedString(forKey: "l6_title"),
            navigationTitle: LanguageManager.shared.localizedString(forKey: "l6_app_bar"),
            content: LessonContent.contentL6,
            sort: 6
        ),
    ] }
    
    static var itemsL3: [LessonItemList] { [
        .init(
            id: "l7_title",
            title: LanguageManager.shared.localizedString(forKey: "l7_title"),
            navigationTitle: LanguageManager.shared.localizedString(forKey: "l7_app_bar"),
            content: LessonContent.contentL7,
            sort: 7
        ),
        .init(
            id: "l8_title",
            title: LanguageManager.shared.localizedString(forKey: "l8_title"),
            navigationTitle: LanguageManager.shared.localizedString(forKey: "l8_app_bar"),
            content: LessonContent.contentL8,
            sort: 8
        ),
        .init(
            id: "l9_title",
            title: LanguageManager.shared.localizedString(forKey: "l9_title"),
            navigationTitle: LanguageManager.shared.localizedString(forKey: "l9_app_bar"),
            content: LessonContent.contentL9,
            sort: 9
        ),
    ] }
}

extension LessonContent {
    static var contentL1: [LessonContent] { [
        .init(
            headerKey: "l1_title",
            texts: [
                AnyHashableView(LocalizedLessonText("l1_p1")),
                AnyHashableView(LocalizedLessonText("l1_p2"))
            ]
        ),
        .init(
            headerKey: "l1_h2",
            texts: [
                AnyHashableView(LocalizedLessonText("l1_p3")),
                AnyHashableView(LocalizedLessonText("l1_p4")),
            ]
        ),
        .init(
            headerKey: "l1_h3",
            texts: [
                AnyHashableView(LocalizedLessonText("l1_p5")),
                AnyHashableView(LocalizedLessonText("l1_p6")),
            ]
        ),
        .init(
            headerKey: "l1_h4",
            texts: [
                AnyHashableView(LocalizedLessonText("l1_p7")),
                AnyHashableView(LocalizedLessonText("l1_p8")),
                AnyHashableView(LocalizedLessonText("l1_p9")),
            ]
        )
    ] }
    
    static var contentL2: [LessonContent] { [
        .init(
            headerKey: "l2_title",
            texts: [
                AnyHashableView(LocalizedLessonText("l2_p1")),
                AnyHashableView(LocalizedLessonText("l2_p2")),
                AnyHashableView(LocalizedLessonText("l2_p3")),
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
                AnyHashableView(LocalizedLessonText("l2_p4")),
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
                AnyHashableView(LocalizedLessonText("l2_p5")),
                AnyHashableView(LocalizedLessonText("l2_p6")),
            ]
        ),
        .init(
            headerKey: "l2_h1",
            texts: [
                AnyHashableView(LocalizedLessonText("l2_p7")),
                AnyHashableView(LocalizedLessonText("l2_p8")),
                AnyHashableView(LocalizedLessonText("l2_p9")),
                AnyHashableView(LocalizedLessonText("l2_p10")),
            ]
        ),
        .init(
            headerKey: "l2_h2",
            texts: [
                AnyHashableView(LocalizedLessonText("l2_p11")),
                AnyHashableView(LocalizedLessonText("l2_p12")),
                AnyHashableView(LocalizedLessonText("l2_p13")),
            ]
        )
    ] }
    
    static var contentL3: [LessonContent] { [
            .init(
                headerKey: "l3_title",
                texts: [
                    AnyHashableView(LocalizedLessonText("l3_p1")),
                    AnyHashableView(LocalizedLessonText("l3_p2")),
                ]
            )
        ] }
        
    static var contentL4: [LessonContent] { [
            .init(
                headerKey: "l4_title",
                texts: [
                    AnyHashableView(LocalizedLessonText("l4_p1")),
                    AnyHashableView(LocalizedLessonText("l4_p2")),
                ]
            ),
            .init(
                headerKey: "l4_h1",
                texts: [
                    AnyHashableView(LocalizedLessonText("l4_p3")),
                    AnyHashableView(LocalizedLessonText("l4_p4")),
                ]
            ),
            .init(
                headerKey: "l4_h2",
                texts: [
                    AnyHashableView(LocalizedLessonText("l4_p5")),
                    AnyHashableView(LocalizedLessonText("l4_p6")),
                ]
            )
        ] }
        
    static var contentL5: [LessonContent] { [
            .init(
                headerKey: "l5_title",
                texts: [
                    AnyHashableView(LocalizedLessonText("l5_p1")),
                    AnyHashableView(LocalizedLessonText("l5_p2")),
                ]
            ),
            .init(
                headerKey: "l5_h1",
                texts: [
                    AnyHashableView(LocalizedLessonText("l5_p3")),
                    AnyHashableView(LocalizedLessonText("l5_p4")),
                ]
            ),
            .init(
                headerKey: "l5_h2",
                texts: [
                    AnyHashableView(LocalizedLessonText("l5_p5")),
                ]
            )
        ] }
        
    static var contentL6: [LessonContent] { [
            .init(
                headerKey: "l6_title",
                texts: [
                    AnyHashableView(LocalizedLessonText("l6_p1")),
                ]
            ),
            .init(
                headerKey: "l6_h1",
                texts: [
                    AnyHashableView(LocalizedLessonText("l6_p3")),
                    AnyHashableView(LocalizedLessonText("l6_p4")),
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
                headerKey: "l6_h2",
                texts: [
                    AnyHashableView(LocalizedLessonText("l6_p5")),
                    AnyHashableView(LocalizedLessonText("l6_p6")),
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
                    AnyHashableView(LocalizedLessonText("l6_p7")),
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
                headerKey: "l6_h3",
                texts: [
                    AnyHashableView(LocalizedLessonText("l6_p8")),
                    AnyHashableView(LocalizedLessonText("l6_p9")),
                    AnyHashableView(LocalizedLessonText("l6_p10")),
                    AnyHashableView(LocalizedLessonText("l6_p11")),
                ]
            )
        ] }
        
    static var contentL7: [LessonContent] { [
            .init(
                headerKey: "l7_title",
                texts: [
                    AnyHashableView(LocalizedLessonText("l7_p1")),
                    AnyHashableView(LocalizedLessonText("l7_p2")),
                    AnyHashableView(LocalizedLessonText("l7_p3")),
                    AnyHashableView(LocalizedLessonText("l7_p4")),
                ]
            ),
            .init(
                headerKey: "l7_h1",
                texts: [
                    AnyHashableView(LocalizedLessonText("l7_p5")),
                    AnyHashableView(LocalizedLessonText("l7_p6")),
                    AnyHashableView(LocalizedLessonText("l7_p7")),
                ]
            )
        ] }
        
    static var contentL8: [LessonContent] { [
            .init(
                headerKey: "l8_title",
                texts: [
                    AnyHashableView(LocalizedLessonText("l8_p1")),
                    AnyHashableView(LocalizedLessonText("l8_p2")),
                    AnyHashableView(LocalizedLessonText("l8_p3")),
                    AnyHashableView(LocalizedLessonText("l8_p4")),
                    AnyHashableView(LocalizedLessonText("l8_p5")),
                ]
            ),
            .init(
                headerKey: "l8_h1",
                texts: [
                    AnyHashableView(LocalizedLessonText("l8_p6")),
                    AnyHashableView(LocalizedLessonText("l8_p7")),
                    AnyHashableView(LocalizedLessonText("l8_p8")),
                ]
            ),
        ] }
        
    static var contentL9: [LessonContent] { [
            .init(
                headerKey: "l9_h1",
                texts: [
                    AnyHashableView(LocalizedLessonText("l9_p1")),
                    AnyHashableView(LocalizedLessonText("l9_p2")),
                ]
            ),
            .init(
                headerKey: "l9_h2",
                texts: [
                    AnyHashableView(LocalizedLessonText("l9_p3")),
                    AnyHashableView(LocalizedLessonText("l9_p4")),
                    AnyHashableView(LocalizedLessonText("l9_p5")),
                ]
            ),
            .init(
                headerKey: "l9_h3",
                texts: [
                    AnyHashableView(LocalizedLessonText("l9_p6")),
                    AnyHashableView(LocalizedLessonText("l9_p7")),
                    AnyHashableView(LocalizedLessonText("l9_p8")),
                    AnyHashableView(LocalizedLessonText("l9_p9")),
                    AnyHashableView(LocalizedLessonText("l9_p10")),
                    AnyHashableView(LocalizedLessonText("l9_p11")),
                ]
            ),
        ] }
}
