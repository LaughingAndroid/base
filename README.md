
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    lintOptions {
        abortOnError false
    }


}
apply plugin: 'me.tatarka.retrolambda'

dependencies {
    retrolambdaConfig 'net.orfjackal.retrolambda:retrolambda:2.3.0'
    compile fileTree(include: ['*.jar'], dir: 'libs')
    testCompile 'junit:junit:4.12'
    compile "com.android.support:appcompat-v7:$rootProject.supportLibraryVersion"
    compile "com.android.support:recyclerview-v7:$rootProject.supportLibraryVersion"
    compile "io.reactivex:rxjava:$rootProject.rxVersion"
    compile "io.reactivex:rxandroid:$rootProject.rxAndroidVersion"
    // view注解
    compile 'com.jakewharton:butterknife:8.0.1'
    compile 'com.jakewharton:butterknife-compiler:8.0.1'

    compile "com.android.support:appcompat-v7:$rootProject.supportLibraryVersion"
    compile "com.android.support:recyclerview-v7:$rootProject.supportLibraryVersion"
    compile "com.android.support:design:$rootProject.supportLibraryVersion"
    compile "com.android.support:support-v4:$rootProject.supportLibraryVersion"
    compile "com.android.support:cardview-v7:$rootProject.supportLibraryVersion"
    compile "com.android.support:gridlayout-v7:$rootProject.supportLibraryVersion"

    // recycle view divider库 https://github.com/yqritc/RecyclerView-FlexibleDivider
    compile 'com.yqritc:recyclerview-flexibledivider:1.2.9'
    compile 'com.squareup.retrofit2:converter-gson:2.1.0'
