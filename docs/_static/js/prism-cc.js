Prism.languages.cc = {
    'entity': /`[^`]+`/i,
    'bold': {
        pattern: /^(([\w]+)!?)(\([^)]+\))?: ([^\n]+)/i,
        inside: {
            'number': /#\d+/,
            'constant': {
                pattern: /^(([\w]+)!?)/i,
                inside: {
                    important: /!/,
                }
            },
            'attr-value': /\([^)]+\)/i,
        }
    },
    'comment': {
        pattern: /([\r\n]{2})((.+([\r\n]{0,2}))*)?/i,
        inside: {
            'number': /(#\d+)|([a-f0-9]{7,})/i,
            'italic': {
                pattern: /([\r\n]+)([-]{8,})([\r\n]+(# Locales)[\r\n]+)((.+?([\r\n]{0,2}))+?)(?=([-]{8,})|$)/i,
                inside: {
                    'string': {
                        pattern: /^-.+$/im,
                        inside: {
                            'symbol': /-(?= \*\*\[)/,
                            'variable': /\*\*\[[\w\-]{2,10}]\*\*/i,
                        }
                    }
                }
            },
            'prolog': {
                pattern: /[\r\n]{1,2}.+/,
                inside: {
                    'bold': /(BREAKING CHANGE|DEPRECATED):/
                }
            },
        }
    },
};
