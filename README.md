# Lexy
Lexy is a general purpose "tokenizer"

However, Lexy is capable of more than just tokenizing

Lexy has a generified data structure, allowing it to work on arbitrary data, and return arbitrary data

Due to this, it can also be used as an AST parser, granted it's harder to use it as that than as a tokenizer

# ASTral
ASTral is a language which will be parsed by a lexy parser, to generate AST parsers

ASTral was inspired by regex

The following is an example parser:
```
TERMIN_MEMBERS=(([("private"),(":")],[("public"),(":")]),("}")).STRUCT=CLASS[[("struct","class"),TYPE(GENERIC)],CODE_BLOCK[GENERIC(LBRACE),<^.,(PRIVATE_MEMBERS(<KEYWORD[[("private"),(":")]],(${DEF},^),${TERMIN_MEMBERS}>),PUBLIC_MEMBERS(<KEYWORD[[("public"),(":")]],(${DEF},^),${TERMIN_MEMBERS}>),DEFAULT_MEMBERS(<^.,(${DEF},^),${TERMIN_MEMBERS}>)),("}").>?,GENERIC(RBRACE)],NAME(GENERIC)?]_DEF=DEFINITION[(KEYWORD("extern","volatile","virtual","final","static","const","override","constexpr","friend","__stdcall"),ACCESS[DECLSPEC("__declspec"),PARENTH[GENERIC(LPARENTH),GENERIC!((RPARENTH)),GENERIC(RPARENTH)]?])+?,${TYPED_VAR},${METHOD_BODY}?]_TYPED_VAR=[TYPE[GENERIC(GENERIC,KEYWORD),GENERIC("*")+?],${VAR}]VAR=VAR[(PARENTH[GENERIC(LPARENTH)+,GENERIC("*"),NAME!(RPARENTH),GENERIC(RPARENTH)+],NAME[GENERIC(GENERIC)]),(PARENTH[GENERIC(LPARENTH),[DEFINITION(${TYPED_VAR}),GENERIC(COMMA)]+?,DEFINITION(${TYPED_VAR}?),GENERIC(RPARENTH)],ARRAY[GENERIC(LBRACKET),GENERIC(NUMBER)?,GENERIC(RBRACKET)]+)?,VALUE[(ASSIGNMENT),${STATEMENT}?]?]FLOW_CONTROL=FLOW_CONTROL([(["else","if"],"if","while","for"),PARENTH[LPARENTH,[${STATEMENT}?,(SEMICOLON)?]:+,RPARENTH],(${METHOD_BODY},${STATEMENT})?],[("else","do"),(${METHOD_BODY},${STATEMENT})?])METHOD_BODY=CODE_BLOCK[GENERIC(LBRACE),<^.,(${FLOW_CONTROL},${METHOD_BODY},${STATEMENT},^),(RBRACE).>?,GENERIC(RBRACE)]STATEMENT=STATEMENT(<^.,(${DEF},^),(RBRACE,RPARENTH,SEMICOLON).>:)ROOT=(COMMENT(LINE_COMMENT),COMMENT(BLOCK_COMMENT),WHITE_SPACE(WHITE_SPACE),${STRUCT},${DEF},^)+
```
and with comments and formatting:
```
#
    Matchers:
        (  ) match any
        [  ] match all
        ("") match text
       ${  } match other matcher
       <   > from cont until
           ^ anything; equivalent to ![!(()?)]
    Mutators
        !() inverts the match, can only match one token at a time
        ()+ match repeating (greedy)
        ()? match optional
        ()_ filter whitespace
        ()* don't filter whitespace
        (). match without progressing
        (): require progression

    (), [], and ! can create named branches
    This is done by putting the name of the branch type before the symbol

    Ex:
        GENERIC!(KEYWORD)
            Remaps non-keyword tokens to generic branches

        You can have as many conditions in "match any" () or "match all" [] as you want

        (KEYWORD, GENERIC)
            Will match any token which is either a keyword, or a generic token, favoring keywords
                if you had a more complex condition, which the first condition can also match, and both are true, it'll select the first condition
                usually, it's best to order conditions from most specific to least specific due to this

        [KEYWORD, GENERIC]
            Will match any token which is followed by a generic token

        <(LPARENTH), ^, (RPARENTH)>
            Matches all tokens between a LPARENTH and RPARENTH token
                the cont pattern must never fail to match a token, or an error will be thrown

    Tips:
         ^. Can be used to unconditionally start a <from,cont,until> pattern without causing any missed tokens

        (): can be used to ensure that a pattern moves the matcher, even if you don't know if it will be matched ahead of time
            Let's say DEF can sometimes return an empty match
            You can put a : following it to ensure that it is not empty before matching it
            <^., (${DEF}:, ^), ${TERMIN})

        Whitespace has no meaning at all, you can format stuff however you want

        ()?+ will cause problems:
                Optional returns an empty match instead of no match if its matcher doesn't pass
                Greedy validates that a match isn't empty match and throws an error if it is empty
                ()?:+ would work, but it's not the best for performance
                 ()+? is the intended solution
#

TERMIN_MEMBERS = (
    (
        [("private"), (":")],
        [("public"), (":")]
    ),
    ("}")
).

STRUCT = CLASS[
    [
        ("struct", "class"),
        TYPE(GENERIC)
    ],
    CODE_BLOCK[
        GENERIC(LBRACE),
        <
            ^.,
            # TODO: support constructors #
            (
                PRIVATE_MEMBERS(<
                    KEYWORD[
                        [("private"), (":")]
                    ],
                    (${DEF}, ^),
                    ${TERMIN_MEMBERS}
                >),
                PUBLIC_MEMBERS(<
                    KEYWORD[
                        [("public"), (":")]
                    ],
                    (${DEF}, ^),
                    ${TERMIN_MEMBERS}
                >),
                DEFAULT_MEMBERS(<
                    ^.,
                    (${DEF}, ^),
                    ${TERMIN_MEMBERS}
                >)
            ),
            ("}").
        >?,
        GENERIC(RBRACE)
    ],
    NAME(GENERIC)?
]_

DEF = DEFINITION[
    # access&such #
    (
        KEYWORD("extern", "volatile", "virtual", "final", "static", "const", "override", "constexpr", "friend", "__stdcall"),
        ACCESS[
            DECLSPEC("__declspec"),
            PARENTH[
                GENERIC(LPARENTH),
                GENERIC!((RPARENTH)),
                GENERIC(RPARENTH)
            ]?
        ]
    )+?,
    ${TYPED_VAR},
    ${METHOD_BODY}?
]_

TYPED_VAR = [
    # type&ptrs #
    TYPE[
        GENERIC(GENERIC, KEYWORD),
        GENERIC("*")+?
    ],
    ${VAR}
]

VAR = VAR[
    # name #
    (
        # func ptr #
        # TODO: finish this #
        PARENTH[
            GENERIC(LPARENTH)+,
            GENERIC("*"),
            NAME!(RPARENTH),
            GENERIC(RPARENTH)+
        ],
        # regular #
        NAME[
            GENERIC(GENERIC)
        ]
    ),
    (
        # params #
        PARENTH[
            GENERIC(LPARENTH),
            [
                DEFINITION(${TYPED_VAR}),
                GENERIC(COMMA)
            ]+?,
            DEFINITION(${TYPED_VAR}?),
            GENERIC(RPARENTH)
        ],
        # array #
        ARRAY[
            GENERIC(LBRACKET),
            GENERIC(NUMBER)?,
            GENERIC(RBRACKET)
        ]+
    )?,
    # value #
    VALUE[
        (ASSIGNMENT),
        ${STATEMENT}?
    ]?
]

FLOW_CONTROL = FLOW_CONTROL(
    [
        (["else", "if"], "if", "while", "for"),
        PARENTH[
            LPARENTH,
            [${STATEMENT}?, (SEMICOLON)?]:+,
            RPARENTH
        ],
        (
            ${METHOD_BODY},
            ${STATEMENT}
        )?
    ],
    [
        ("else", "do"),
        (
            ${METHOD_BODY},
            ${STATEMENT}
        )?
    ]
)

METHOD_BODY = CODE_BLOCK[
    GENERIC(LBRACE),
    # collect statements, flow controls, and code blocks #
    <
        ^.,
        (
            ${FLOW_CONTROL},
            ${METHOD_BODY}, # consumes }, preventing the loop from ending early #
            ${STATEMENT},
            ^
        ),
        # until any } #
        (RBRACE).
    >?,
    GENERIC(RBRACE)
]

STATEMENT = STATEMENT(
    <
        ^.,
        (
            ${DEF},
            ^
        ),
        # until any ) } or ; #
        (RBRACE, RPARENTH, SEMICOLON).
    >:
)

ROOT = (
    COMMENT(LINE_COMMENT),
    COMMENT(BLOCK_COMMENT),
    WHITE_SPACE(WHITE_SPACE),
    ${STRUCT},
    ${DEF},
    ^
)+
```
