Prism.languages.cc = {
    'number': /#\d+/,
    'bold': {
        pattern: /^([a-z!]+?)(\([\w-$]+\))?: .+/i,
        inside: {
            'constant': {
                pattern: /^([a-z]+)!?/i,
                inside: {
                    important: /!/,
                }
            },
            'attr-value': /\([\w-$]+\)/i,
        }
    },
    'comment': {
        pattern: /[\r\n]{1,2}.+/,
        inside: {
            'comment': /((closes issue)|(issue \/close)) .+/,
            'prolog': {
                pattern: /[\r\n]{1,2}.+/,
                inside: {
                    'bold': /BREAKING CHANGE|DEPRECATION/,
                    'italic': {
                        pattern: /([a-z]+)!?(\([\w-$]+\))?: .+/i,
                    }
                }
            }
        }
    },
};
