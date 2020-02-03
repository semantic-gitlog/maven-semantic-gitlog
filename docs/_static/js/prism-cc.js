Prism.languages.cc = {
    'bold': {
        pattern: /^(([\w]+)!?)(\(((\w+\/)*)([\w-$_]+)\))?: ([^\n]+)/i,
        inside: {
            'constant': {
                pattern: /^(([\w]+)!?)/i,
                inside: {
                    important: /!/,
                }
            },
            'attr-value': /\(((\w+\/)*)([\w-$_]+)\)/i,
            'number': /#\d+/,
        }
    },
    'comment': {
        pattern: /([\r\n]{2})((.+([\r\n]{0,2}))*)?/i,
        inside: {
            'number': /(#\d+)|([a-f0-9]{7,})/i,
            'prolog': {
                pattern: /[\r\n]{1,2}.+/,
                inside: {
                    'bold': /(BREAKING CHANGE|DEPRECATED):/
                }
            },
        }
    },
};
