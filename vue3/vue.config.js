const path = require('path');
module.exports = {
    pluginOptions: {
        i18n: {
            locale: 'en',
            fallbackLocale: 'en',
            localeDir: 'locales',
            enableInSFC: false
        }
    },
    chainWebpack: config => {
        // Remove prefetch plugin and that's it!
        config.plugins.delete('prefetch');
        config.resolve.alias.set('vue', '@vue/runtime-dom')
        config.module
            .rule('json')
            .test(/\.json$/)
            .use('json-loader')
            .loader('json-loader')
            .end()
            .rule('js')
            .test(/\.m?js$/)
            .exclude
            .add(file => (
                /node_modules/.test(file) &&
                !/\.vue\.js/.test(file)
            ))
            .end()
            .use('babel-loader')
            .loader('babel-loader')
            .end()
            .rule('plotly')
            .test(/plotly\.js-basic-dist/)
            .use('plotly-loader')
            .loader('plotly-loader')
            .end()
    },
    configureWebpack: {
        resolve: {
            alias: {
                '@themeConfig': path.resolve(__dirname, 'theme.config.js'),                
            }
        },
        externals: {
      'vue': 'Vue',
      'plotly.js': 'Plotly'
    }
    }
};
