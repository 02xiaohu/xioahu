'use strict';

Object.defineProperty(exports, '__esModule', { value: true });

var fs = require('fs');

/**
 * @license
 * Copyright Google Inc. All Rights Reserved.
 *
 * Use of this source code is governed by an MIT-style license that can be
 * found in the LICENSE file at https://angular.io/license
 */
function findArgument(argv, argName) {
    const index = argv.indexOf(argName);
    if (index < 0 || index === argv.length - 1) {
        return;
    }
    return argv[index + 1];
}
function parseStringArray(argv, argName) {
    const arg = findArgument(argv, argName);
    if (!arg) {
        return [];
    }
    return arg.split(',');
}
function hasArgument(argv, argName) {
    return argv.includes(argName);
}
function parseCommandLine(argv) {
    return {
        help: hasArgument(argv, '--help'),
        ivy: hasArgument(argv, '--experimental-ivy'),
        logFile: findArgument(argv, '--logFile'),
        logVerbosity: findArgument(argv, '--logVerbosity'),
        logToConsole: hasArgument(argv, '--logToConsole'),
        ngProbeLocations: parseStringArray(argv, '--ngProbeLocations'),
        tsProbeLocations: parseStringArray(argv, '--tsProbeLocations'),
    };
}

/**
 * @license
 * Copyright Google Inc. All Rights Reserved.
 *
 * Use of this source code is governed by an MIT-style license that can be
 * found in the LICENSE file at https://angular.io/license
 */
const MIN_TS_VERSION = '4.1';
function resolve(packageName, location, rootPackage) {
    rootPackage = rootPackage || packageName;
    try {
        const packageJsonPath = require.resolve(`${rootPackage}/package.json`, {
            paths: [location],
        });
        // Do not use require() to read JSON files since it's a potential security
        // vulnerability.
        const packageJson = JSON.parse(fs.readFileSync(packageJsonPath, 'utf8'));
        const resolvedPath = require.resolve(packageName, {
            paths: [location],
        });
        return {
            name: packageName,
            resolvedPath,
            version: new Version(packageJson.version),
        };
    }
    catch (_a) {
    }
}
/**
 * Resolve the node module with the specified `packageName` that satisfies
 * the specified minimum version.
 * @param packageName name of package to be resolved
 * @param minVersionStr minimum version
 * @param probeLocations locations to initiate node module resolution
 * @param rootPackage location of package.json. For example, the root package of
 * `typescript/lib/tsserverlibrary` is `typescript`.
 */
function resolveWithMinVersion(packageName, minVersionStr, probeLocations, rootPackage) {
    if (!packageName.startsWith(rootPackage)) {
        throw new Error(`${packageName} must be in the root package`);
    }
    const minVersion = new Version(minVersionStr);
    for (const location of probeLocations) {
        const nodeModule = resolve(packageName, location, rootPackage);
        if (nodeModule && nodeModule.version.greaterThanOrEqual(minVersion)) {
            return nodeModule;
        }
    }
    throw new Error(`Failed to resolve '${packageName}' with minimum version '${minVersion}' from ` +
        JSON.stringify(probeLocations, null, 2));
}
/**
 * Resolve `typescript/lib/tsserverlibrary` from the given locations.
 * @param probeLocations
 */
function resolveTsServer(probeLocations) {
    const tsserver = 'typescript/lib/tsserverlibrary';
    return resolveWithMinVersion(tsserver, MIN_TS_VERSION, probeLocations, 'typescript');
}
/**
 * Converts the specified string `a` to non-negative integer.
 * Returns -1 if the result is NaN.
 * @param a
 */
function parseNonNegativeInt(a) {
    // parseInt() will try to convert as many as possible leading characters that
    // are digits. This means a string like "123abc" will be converted to 123.
    // For our use case, this is sufficient.
    const i = parseInt(a, 10 /* radix */);
    return isNaN(i) ? -1 : i;
}
class Version {
    constructor(versionStr) {
        this.versionStr = versionStr;
        const [major, minor, patch] = Version.parseVersionStr(versionStr);
        this.major = major;
        this.minor = minor;
        this.patch = patch;
    }
    greaterThanOrEqual(other) {
        if (this.major < other.major) {
            return false;
        }
        if (this.major > other.major) {
            return true;
        }
        if (this.minor < other.minor) {
            return false;
        }
        if (this.minor > other.minor) {
            return true;
        }
        return this.patch >= other.patch;
    }
    toString() {
        return this.versionStr;
    }
    /**
     * Converts the specified `versionStr` to its number constituents. Invalid
     * number value is represented as negative number.
     * @param versionStr
     */
    static parseVersionStr(versionStr) {
        const [major, minor, patch] = versionStr.split('.').map(parseNonNegativeInt);
        return [
            major === undefined ? 0 : major,
            minor === undefined ? 0 : minor,
            patch === undefined ? 0 : patch,
        ];
    }
}

/**
 * This method provides a custom implementation for the AMD loader to resolve
 * `typescript` module at runtime.
 * @param modules modules to resolve
 * @param cb function to invoke with resolved modules
 */
function define(modules, cb) {
    const TSSERVER = 'typescript/lib/tsserverlibrary';
    const resolvedModules = modules.map(m => {
        if (m === 'typescript') {
            throw new Error(`Import '${TSSERVER}' instead of 'typescript'`);
        }
        if (m === TSSERVER) {
            const { tsProbeLocations } = parseCommandLine(process.argv);
            m = resolveTsServer(tsProbeLocations).resolvedPath;
        }
        return require(m);
    });
    cb(...resolvedModules);
}

exports.define = define;

define(['fs', 'path', 'typescript/lib/tsserverlibrary', 'vscode-languageserver/node', 'vscode-jsonrpc', 'vscode-languageserver', 'vscode-uri', 'child_process'], function (fs, path, tsserverlibrary, node, vscodeJsonrpc, vscodeLanguageserver, vscodeUri, child_process) { 'use strict';

	function _interopDefaultLegacy (e) { return e && typeof e === 'object' && 'default' in e ? e : { 'default': e }; }

	var fs__default = /*#__PURE__*/_interopDefaultLegacy(fs);
	var path__default = /*#__PURE__*/_interopDefaultLegacy(path);
	var tsserverlibrary__default = /*#__PURE__*/_interopDefaultLegacy(tsserverlibrary);
	var node__default = /*#__PURE__*/_interopDefaultLegacy(node);
	var vscodeJsonrpc__default = /*#__PURE__*/_interopDefaultLegacy(vscodeJsonrpc);
	var vscodeLanguageserver__default = /*#__PURE__*/_interopDefaultLegacy(vscodeLanguageserver);
	var vscodeUri__default = /*#__PURE__*/_interopDefaultLegacy(vscodeUri);
	var child_process__default = /*#__PURE__*/_interopDefaultLegacy(child_process);

	function unwrapExports (x) {
		return x && x.__esModule && Object.prototype.hasOwnProperty.call(x, 'default') ? x['default'] : x;
	}

	function createCommonjsModule(fn, module) {
		return module = { exports: {} }, fn(module, module.exports), module.exports;
	}

	var cmdline_utils = createCommonjsModule(function (module, exports) {
	/**
	 * @license
	 * Copyright Google Inc. All Rights Reserved.
	 *
	 * Use of this source code is governed by an MIT-style license that can be
	 * found in the LICENSE file at https://angular.io/license
	 */
	Object.defineProperty(exports, "__esModule", { value: true });
	exports.generateHelpMessage = exports.parseCommandLine = void 0;
	function findArgument(argv, argName) {
	    const index = argv.indexOf(argName);
	    if (index < 0 || index === argv.length - 1) {
	        return;
	    }
	    return argv[index + 1];
	}
	function parseStringArray(argv, argName) {
	    const arg = findArgument(argv, argName);
	    if (!arg) {
	        return [];
	    }
	    return arg.split(',');
	}
	function hasArgument(argv, argName) {
	    return argv.includes(argName);
	}
	function parseCommandLine(argv) {
	    return {
	        help: hasArgument(argv, '--help'),
	        ivy: hasArgument(argv, '--experimental-ivy'),
	        logFile: findArgument(argv, '--logFile'),
	        logVerbosity: findArgument(argv, '--logVerbosity'),
	        logToConsole: hasArgument(argv, '--logToConsole'),
	        ngProbeLocations: parseStringArray(argv, '--ngProbeLocations'),
	        tsProbeLocations: parseStringArray(argv, '--tsProbeLocations'),
	    };
	}
	exports.parseCommandLine = parseCommandLine;
	function generateHelpMessage(argv) {
	    return `Angular Language Service that implements the Language Server Protocol (LSP).

  Usage: ${argv[0]} ${argv[1]} [options]

  Options:
    --help: Prints help message.
    --experimental-ivy: Enables the Ivy language service. Defaults to false.
    --logFile: Location to log messages. Logging to file is disabled if not provided.
    --logVerbosity: terse|normal|verbose|requestTime. See ts.server.LogLevel.
    --logToConsole: Enables logging to console via 'window/logMessage'. Defaults to false.
    --ngProbeLocations: Path of @angular/language-service. Required.
    --tsProbeLocations: Path of typescript. Required.

  Additional options supported by vscode-languageserver:
    --clientProcessId=<number>: Automatically kills the server if the client process dies.
    --node-ipc: Communicate using Node's IPC. This is the default.
    --stdio: Communicate over stdin/stdout.
    --socket=<number>: Communicate using Unix socket.
  `;
	}
	exports.generateHelpMessage = generateHelpMessage;

	});

	unwrapExports(cmdline_utils);
	cmdline_utils.generateHelpMessage;
	cmdline_utils.parseCommandLine;

	var logger = createCommonjsModule(function (module, exports) {
	/**
	 * @license
	 * Copyright Google Inc. All Rights Reserved.
	 *
	 * Use of this source code is governed by an MIT-style license that can be
	 * found in the LICENSE file at https://angular.io/license
	 */
	Object.defineProperty(exports, "__esModule", { value: true });
	exports.createLogger = void 0;



	/**
	 * Create a logger instance to write to file.
	 * @param options Logging options.
	 */
	function createLogger(options) {
	    let logLevel;
	    switch (options.logVerbosity) {
	        case 'requestTime':
	            logLevel = tsserverlibrary__default['default'].server.LogLevel.requestTime;
	            break;
	        case 'verbose':
	            logLevel = tsserverlibrary__default['default'].server.LogLevel.verbose;
	            break;
	        case 'normal':
	            logLevel = tsserverlibrary__default['default'].server.LogLevel.normal;
	            break;
	        case 'terse':
	        default:
	            logLevel = tsserverlibrary__default['default'].server.LogLevel.terse;
	            break;
	    }
	    return new Logger(logLevel, options.logFile);
	}
	exports.createLogger = createLogger;
	// TODO: Code below is from TypeScript's repository. Maybe create our own
	// implementation.
	// https://github.com/microsoft/TypeScript/blob/ec39d412876d0dcf704fc886d5036cb625220d2f/src/tsserver/server.ts#L120
	function noop(_) { } // tslint:disable-line no-empty
	function nowString() {
	    // E.g. "12:34:56.789"
	    const d = new Date();
	    return `${d.getHours()}:${d.getMinutes()}:${d.getSeconds()}.${d.getMilliseconds()}`;
	}
	class Logger {
	    constructor(level, logFilename) {
	        this.level = level;
	        this.logFilename = logFilename;
	        this.seq = 0;
	        this.inGroup = false;
	        this.firstInGroup = true;
	        this.fd = -1;
	        if (logFilename) {
	            try {
	                const dir = path__default['default'].dirname(logFilename);
	                if (!fs__default['default'].existsSync(dir)) {
	                    fs__default['default'].mkdirSync(dir);
	                }
	                this.fd = fs__default['default'].openSync(logFilename, 'w');
	            }
	            catch (_a) {
	                // swallow the error and keep logging disabled if file cannot be opened
	            }
	        }
	    }
	    static padStringRight(str, padding) {
	        return (str + padding).slice(0, padding.length);
	    }
	    close() {
	        if (this.loggingEnabled()) {
	            fs__default['default'].close(this.fd, noop);
	        }
	    }
	    getLogFileName() {
	        return this.logFilename;
	    }
	    perftrc(s) {
	        this.msg(s, tsserverlibrary__default['default'].server.Msg.Perf);
	    }
	    info(s) {
	        this.msg(s, tsserverlibrary__default['default'].server.Msg.Info);
	    }
	    startGroup() {
	        this.inGroup = true;
	        this.firstInGroup = true;
	    }
	    endGroup() {
	        this.inGroup = false;
	    }
	    loggingEnabled() {
	        return this.fd >= 0;
	    }
	    hasLevel(level) {
	        return this.loggingEnabled() && this.level >= level;
	    }
	    msg(s, type = tsserverlibrary__default['default'].server.Msg.Err) {
	        if (!this.loggingEnabled()) {
	            return;
	        }
	        s = `[${nowString()}] ${s}\n`;
	        if (!this.inGroup || this.firstInGroup) {
	            const prefix = Logger.padStringRight(type + ' ' + this.seq.toString(), '          ');
	            s = prefix + s;
	        }
	        const buf = Buffer.from(s);
	        fs__default['default'].writeSync(this.fd, buf, 0, buf.length);
	        if (!this.inGroup) {
	            this.seq++;
	        }
	    }
	}

	});

	unwrapExports(logger);
	logger.createLogger;

	var server_host = createCommonjsModule(function (module, exports) {
	/**
	 * @license
	 * Copyright Google Inc. All Rights Reserved.
	 *
	 * Use of this source code is governed by an MIT-style license that can be
	 * found in the LICENSE file at https://angular.io/license
	 */
	Object.defineProperty(exports, "__esModule", { value: true });
	exports.ServerHost = void 0;

	/**
	 * `ServerHost` is a wrapper around `ts.sys` for the Node system. In Node, all
	 * optional methods of `ts.System` are implemented.
	 * See
	 * https://github.com/microsoft/TypeScript/blob/ec39d412876d0dcf704fc886d5036cb625220d2f/src/compiler/sys.ts#L716
	 */
	class ServerHost {
	    constructor() {
	        this.args = tsserverlibrary__default['default'].sys.args;
	        this.newLine = tsserverlibrary__default['default'].sys.newLine;
	        this.useCaseSensitiveFileNames = tsserverlibrary__default['default'].sys.useCaseSensitiveFileNames;
	    }
	    write(s) {
	        tsserverlibrary__default['default'].sys.write(s);
	    }
	    writeOutputIsTTY() {
	        return tsserverlibrary__default['default'].sys.writeOutputIsTTY();
	    }
	    readFile(path, encoding) {
	        return tsserverlibrary__default['default'].sys.readFile(path, encoding);
	    }
	    getFileSize(path) {
	        return tsserverlibrary__default['default'].sys.getFileSize(path);
	    }
	    writeFile(path, data, writeByteOrderMark) {
	        return tsserverlibrary__default['default'].sys.writeFile(path, data, writeByteOrderMark);
	    }
	    /**
	     * @pollingInterval - this parameter is used in polling-based watchers and
	     * ignored in watchers that use native OS file watching
	     */
	    watchFile(path, callback, pollingInterval) {
	        return tsserverlibrary__default['default'].sys.watchFile(path, callback, pollingInterval);
	    }
	    watchDirectory(path, callback, recursive) {
	        return tsserverlibrary__default['default'].sys.watchDirectory(path, callback, recursive);
	    }
	    resolvePath(path) {
	        return tsserverlibrary__default['default'].sys.resolvePath(path);
	    }
	    fileExists(path) {
	        return tsserverlibrary__default['default'].sys.fileExists(path);
	    }
	    directoryExists(path) {
	        return tsserverlibrary__default['default'].sys.directoryExists(path);
	    }
	    createDirectory(path) {
	        return tsserverlibrary__default['default'].sys.createDirectory(path);
	    }
	    getExecutingFilePath() {
	        return tsserverlibrary__default['default'].sys.getExecutingFilePath();
	    }
	    getCurrentDirectory() {
	        return tsserverlibrary__default['default'].sys.getCurrentDirectory();
	    }
	    getDirectories(path) {
	        return tsserverlibrary__default['default'].sys.getDirectories(path);
	    }
	    readDirectory(path, extensions, exclude, include, depth) {
	        return tsserverlibrary__default['default'].sys.readDirectory(path, extensions, exclude, include, depth);
	    }
	    getModifiedTime(path) {
	        return tsserverlibrary__default['default'].sys.getModifiedTime(path);
	    }
	    setModifiedTime(path, time) {
	        return tsserverlibrary__default['default'].sys.setModifiedTime(path, time);
	    }
	    deleteFile(path) {
	        return tsserverlibrary__default['default'].sys.deleteFile(path);
	    }
	    /**
	     * A good implementation is node.js' `crypto.createHash`.
	     * (https://nodejs.org/api/crypto.html#crypto_crypto_createhash_algorithm)
	     */
	    createHash(data) {
	        return tsserverlibrary__default['default'].sys.createHash(data);
	    }
	    /**
	     * This must be cryptographically secure. Only implement this method using
	     * `crypto.createHash("sha256")`.
	     */
	    createSHA256Hash(data) {
	        return tsserverlibrary__default['default'].sys.createSHA256Hash(data);
	    }
	    getMemoryUsage() {
	        return tsserverlibrary__default['default'].sys.getMemoryUsage();
	    }
	    exit(exitCode) {
	        return tsserverlibrary__default['default'].sys.exit(exitCode);
	    }
	    realpath(path) {
	        return tsserverlibrary__default['default'].sys.realpath(path);
	    }
	    setTimeout(callback, ms, ...args) {
	        return tsserverlibrary__default['default'].sys.setTimeout(callback, ms, ...args);
	    }
	    clearTimeout(timeoutId) {
	        return tsserverlibrary__default['default'].sys.clearTimeout(timeoutId);
	    }
	    clearScreen() {
	        return tsserverlibrary__default['default'].sys.clearScreen();
	    }
	    base64decode(input) {
	        return tsserverlibrary__default['default'].sys.base64decode(input);
	    }
	    base64encode(input) {
	        return tsserverlibrary__default['default'].sys.base64encode(input);
	    }
	    setImmediate(callback, ...args) {
	        return setImmediate(callback, ...args);
	    }
	    clearImmediate(timeoutId) {
	        return clearImmediate(timeoutId);
	    }
	    require(initialPath, moduleName) {
	        try {
	            const modulePath = require.resolve(moduleName, {
	                paths: [initialPath],
	            });
	            return {
	                module: require(modulePath),
	                error: undefined,
	            };
	        }
	        catch (e) {
	            return {
	                module: undefined,
	                error: e,
	            };
	        }
	    }
	}
	exports.ServerHost = ServerHost;

	});

	unwrapExports(server_host);
	server_host.ServerHost;

	var notifications = createCommonjsModule(function (module, exports) {
	/**
	 * @license
	 * Copyright Google Inc. All Rights Reserved.
	 *
	 * Use of this source code is governed by an MIT-style license that can be
	 * found in the LICENSE file at https://angular.io/license
	 */
	Object.defineProperty(exports, "__esModule", { value: true });
	exports.ProjectLanguageService = exports.ProjectLoadingFinish = exports.ProjectLoadingStart = void 0;

	exports.ProjectLoadingStart = new vscodeJsonrpc__default['default'].NotificationType0('angular/projectLoadingStart');
	exports.ProjectLoadingFinish = new vscodeJsonrpc__default['default'].NotificationType0('angular/projectLoadingFinish');
	exports.ProjectLanguageService = new vscodeJsonrpc__default['default'].NotificationType('angular/projectLanguageService');

	});

	unwrapExports(notifications);
	notifications.ProjectLanguageService;
	notifications.ProjectLoadingFinish;
	notifications.ProjectLoadingStart;

	var progress = createCommonjsModule(function (module, exports) {
	/**
	 * @license
	 * Copyright Google Inc. All Rights Reserved.
	 *
	 * Use of this source code is governed by an MIT-style license that can be
	 * found in the LICENSE file at https://angular.io/license
	 */
	Object.defineProperty(exports, "__esModule", { value: true });
	exports.NgccProgressType = exports.NgccProgressToken = void 0;

	exports.NgccProgressToken = 'ngcc';
	exports.NgccProgressType = new vscodeJsonrpc__default['default'].ProgressType();

	});

	unwrapExports(progress);
	progress.NgccProgressType;
	progress.NgccProgressToken;

	var utils = createCommonjsModule(function (module, exports) {
	/**
	 * @license
	 * Copyright Google Inc. All Rights Reserved.
	 *
	 * Use of this source code is governed by an MIT-style license that can be
	 * found in the LICENSE file at https://angular.io/license
	 */
	Object.defineProperty(exports, "__esModule", { value: true });
	exports.isConfiguredProject = exports.lspRangeToTsPositions = exports.lspPositionToTsPosition = exports.tsTextSpanToLspRange = exports.filePathToUri = exports.uriToFilePath = void 0;



	var Scheme;
	(function (Scheme) {
	    Scheme["File"] = "file";
	})(Scheme || (Scheme = {}));
	/**
	 * Extract the file path from the specified `uri`.
	 * @param uri
	 */
	function uriToFilePath(uri) {
	    // Note: uri.path is different from uri.fsPath
	    // See
	    // https://github.com/microsoft/vscode-uri/blob/413805221cc6ed167186ab3103d3248d6f7161f2/src/index.ts#L622-L645
	    const { scheme, fsPath } = vscodeUri__default['default'].URI.parse(uri);
	    if (scheme !== Scheme.File) {
	        return '';
	    }
	    return fsPath;
	}
	exports.uriToFilePath = uriToFilePath;
	/**
	 * Converts the specified `filePath` to a proper URI.
	 * @param filePath
	 */
	function filePathToUri(filePath) {
	    return vscodeUri__default['default'].URI.file(filePath).toString();
	}
	exports.filePathToUri = filePathToUri;
	/**
	 * Convert ts.TextSpan to lsp.TextSpan. TypeScript keeps track of offset using
	 * 1-based index whereas LSP uses 0-based index.
	 * @param scriptInfo Used to determine the offsets.
	 * @param textSpan
	 */
	function tsTextSpanToLspRange(scriptInfo, textSpan) {
	    const start = scriptInfo.positionToLineOffset(textSpan.start);
	    const end = scriptInfo.positionToLineOffset(textSpan.start + textSpan.length);
	    // ScriptInfo (TS) is 1-based, LSP is 0-based.
	    return vscodeLanguageserver__default['default'].Range.create(start.line - 1, start.offset - 1, end.line - 1, end.offset - 1);
	}
	exports.tsTextSpanToLspRange = tsTextSpanToLspRange;
	/**
	 * Convert lsp.Position to the absolute offset in the file. LSP keeps track of
	 * offset using 0-based index whereas TypeScript uses 1-based index.
	 * @param scriptInfo Used to determine the offsets.
	 * @param position
	 */
	function lspPositionToTsPosition(scriptInfo, position) {
	    const { line, character } = position;
	    // ScriptInfo (TS) is 1-based, LSP is 0-based.
	    return scriptInfo.lineOffsetToPosition(line + 1, character + 1);
	}
	exports.lspPositionToTsPosition = lspPositionToTsPosition;
	/**
	 * Convert lsp.Range which is made up of `start` and `end` positions to
	 * TypeScript's absolute offsets.
	 * @param scriptInfo Used to determine the offsets.
	 * @param range
	 */
	function lspRangeToTsPositions(scriptInfo, range) {
	    const start = lspPositionToTsPosition(scriptInfo, range.start);
	    const end = lspPositionToTsPosition(scriptInfo, range.end);
	    return [start, end];
	}
	exports.lspRangeToTsPositions = lspRangeToTsPositions;
	function isConfiguredProject(project) {
	    return project.projectKind === tsserverlibrary__default['default'].server.ProjectKind.Configured;
	}
	exports.isConfiguredProject = isConfiguredProject;

	});

	unwrapExports(utils);
	utils.isConfiguredProject;
	utils.lspRangeToTsPositions;
	utils.lspPositionToTsPosition;
	utils.tsTextSpanToLspRange;
	utils.filePathToUri;
	utils.uriToFilePath;

	var completion = createCommonjsModule(function (module, exports) {
	/**
	 * @license
	 * Copyright Google Inc. All Rights Reserved.
	 *
	 * Use of this source code is governed by an MIT-style license that can be
	 * found in the LICENSE file at https://angular.io/license
	 */
	Object.defineProperty(exports, "__esModule", { value: true });
	exports.tsCompletionEntryToLspCompletionItem = exports.readNgCompletionData = void 0;


	// TODO: Move this to `@angular/language-service`.
	var CompletionKind;
	(function (CompletionKind) {
	    CompletionKind["attribute"] = "attribute";
	    CompletionKind["htmlAttribute"] = "html attribute";
	    CompletionKind["property"] = "property";
	    CompletionKind["component"] = "component";
	    CompletionKind["element"] = "element";
	    CompletionKind["key"] = "key";
	    CompletionKind["method"] = "method";
	    CompletionKind["pipe"] = "pipe";
	    CompletionKind["type"] = "type";
	    CompletionKind["reference"] = "reference";
	    CompletionKind["variable"] = "variable";
	    CompletionKind["entity"] = "entity";
	})(CompletionKind || (CompletionKind = {}));
	/**
	 * Extract `NgCompletionOriginData` from an `lsp.CompletionItem` if present.
	 */
	function readNgCompletionData(item) {
	    if (item.data === undefined) {
	        return null;
	    }
	    // Validate that `item.data.kind` is actually the right tag, and narrow its type in the process.
	    const data = item.data;
	    if (data.kind !== 'ngCompletionOriginData') {
	        return null;
	    }
	    return data;
	}
	exports.readNgCompletionData = readNgCompletionData;
	/**
	 * Convert Angular's CompletionKind to LSP CompletionItemKind.
	 * @param kind Angular's CompletionKind
	 */
	function ngCompletionKindToLspCompletionItemKind(kind) {
	    switch (kind) {
	        case CompletionKind.attribute:
	        case CompletionKind.htmlAttribute:
	        case CompletionKind.property:
	            return vscodeLanguageserver__default['default'].CompletionItemKind.Property;
	        case CompletionKind.component:
	        case CompletionKind.element:
	        case CompletionKind.key:
	            return vscodeLanguageserver__default['default'].CompletionItemKind.Class;
	        case CompletionKind.method:
	            return vscodeLanguageserver__default['default'].CompletionItemKind.Method;
	        case CompletionKind.pipe:
	            return vscodeLanguageserver__default['default'].CompletionItemKind.Function;
	        case CompletionKind.type:
	            return vscodeLanguageserver__default['default'].CompletionItemKind.Interface;
	        case CompletionKind.reference:
	        case CompletionKind.variable:
	            return vscodeLanguageserver__default['default'].CompletionItemKind.Variable;
	        case CompletionKind.entity:
	        default:
	            return vscodeLanguageserver__default['default'].CompletionItemKind.Text;
	    }
	}
	/**
	 * Convert ts.CompletionEntry to LSP Completion Item.
	 * @param entry completion entry
	 * @param position position where completion is requested.
	 * @param scriptInfo
	 */
	function tsCompletionEntryToLspCompletionItem(entry, position, scriptInfo) {
	    const item = vscodeLanguageserver__default['default'].CompletionItem.create(entry.name);
	    // Even though `entry.kind` is typed as ts.ScriptElementKind, it's
	    // really Angular's CompletionKind. This is because ts.ScriptElementKind does
	    // not sufficiently capture the HTML entities.
	    // This is a limitation of being a tsserver plugin.
	    const kind = entry.kind;
	    item.kind = ngCompletionKindToLspCompletionItemKind(kind);
	    item.detail = entry.kind;
	    item.sortText = entry.sortText;
	    // Text that actually gets inserted to the document. It could be different
	    // from 'entry.name'. For example, a method name could be 'greet', but the
	    // insertText is 'greet()'.
	    const insertText = entry.insertText || entry.name;
	    item.textEdit = entry.replacementSpan ?
	        vscodeLanguageserver__default['default'].TextEdit.replace(utils.tsTextSpanToLspRange(scriptInfo, entry.replacementSpan), insertText) :
	        vscodeLanguageserver__default['default'].TextEdit.insert(position, insertText);
	    item.data = {
	        kind: 'ngCompletionOriginData',
	        filePath: scriptInfo.fileName,
	        position,
	    };
	    return item;
	}
	exports.tsCompletionEntryToLspCompletionItem = tsCompletionEntryToLspCompletionItem;

	});

	unwrapExports(completion);
	completion.tsCompletionEntryToLspCompletionItem;
	completion.readNgCompletionData;

	var diagnostic = createCommonjsModule(function (module, exports) {
	/**
	 * @license
	 * Copyright Google Inc. All Rights Reserved.
	 *
	 * Use of this source code is governed by an MIT-style license that can be
	 * found in the LICENSE file at https://angular.io/license
	 */
	Object.defineProperty(exports, "__esModule", { value: true });
	exports.tsDiagnosticToLspDiagnostic = void 0;



	/**
	 * Convert ts.DiagnosticCategory to lsp.DiagnosticSeverity
	 * @param category diagnostic category
	 */
	function tsDiagnosticCategoryToLspDiagnosticSeverity(category) {
	    switch (category) {
	        case tsserverlibrary__default['default'].DiagnosticCategory.Warning:
	            return vscodeLanguageserver__default['default'].DiagnosticSeverity.Warning;
	        case tsserverlibrary__default['default'].DiagnosticCategory.Error:
	            return vscodeLanguageserver__default['default'].DiagnosticSeverity.Error;
	        case tsserverlibrary__default['default'].DiagnosticCategory.Suggestion:
	            return vscodeLanguageserver__default['default'].DiagnosticSeverity.Hint;
	        case tsserverlibrary__default['default'].DiagnosticCategory.Message:
	        default:
	            return vscodeLanguageserver__default['default'].DiagnosticSeverity.Information;
	    }
	}
	/**
	 * Convert ts.Diagnostic to lsp.Diagnostic
	 * @param tsDiag TS diagnostic
	 * @param scriptInfo Used to compute proper offset.
	 */
	function tsDiagnosticToLspDiagnostic(tsDiag, scriptInfo) {
	    const textSpan = {
	        start: tsDiag.start || 0,
	        length: tsDiag.length || 0,
	    };
	    return vscodeLanguageserver__default['default'].Diagnostic.create(utils.tsTextSpanToLspRange(scriptInfo, textSpan), tsserverlibrary__default['default'].flattenDiagnosticMessageText(tsDiag.messageText, '\n'), tsDiagnosticCategoryToLspDiagnosticSeverity(tsDiag.category), tsDiag.code, tsDiag.source);
	}
	exports.tsDiagnosticToLspDiagnostic = tsDiagnosticToLspDiagnostic;

	});

	unwrapExports(diagnostic);
	diagnostic.tsDiagnosticToLspDiagnostic;

	var ngcc = createCommonjsModule(function (module, exports) {
	/**
	 * @license
	 * Copyright Google Inc. All Rights Reserved.
	 *
	 * Use of this source code is governed by an MIT-style license that can be
	 * found in the LICENSE file at https://angular.io/license
	 */
	Object.defineProperty(exports, "__esModule", { value: true });
	exports.resolveAndRunNgcc = void 0;


	function resolveNgccFrom(directory) {
	    try {
	        return require.resolve(`@angular/compiler-cli/ngcc/main-ngcc.js`, {
	            paths: [directory],
	        });
	    }
	    catch (_a) {
	        return null;
	    }
	}
	/**
	 * Resolve ngcc from the directory that contains the specified `tsconfig` and
	 * run ngcc.
	 */
	async function resolveAndRunNgcc(tsconfig, progress) {
	    var _a, _b;
	    const directory = path__default['default'].dirname(tsconfig);
	    const ngcc = resolveNgccFrom(directory);
	    if (!ngcc) {
	        throw new Error(`Failed to resolve ngcc from ${directory}`);
	    }
	    const index = ngcc.lastIndexOf('node_modules');
	    // By default, ngcc assumes the node_modules directory that it needs to process
	    // is in the cwd. In our case, we should set cwd to the directory where ngcc
	    // is resolved to, not the directory where tsconfig.json is located. See
	    // https://github.com/angular/angular/blob/e23fd1f38205410e0ecb601ec73847cea2dea2a8/packages/compiler-cli/ngcc/src/command_line_options.ts#L18-L24
	    const cwd = index > 0 ? ngcc.slice(0, index) : process.cwd();
	    const childProcess = child_process__default['default'].fork(ngcc, [
	        '--tsconfig',
	        tsconfig,
	    ], {
	        cwd: path__default['default'].resolve(cwd),
	    });
	    let stderr = '';
	    (_a = childProcess.stderr) === null || _a === void 0 ? void 0 : _a.on('data', (data) => {
	        stderr += data.toString();
	    });
	    (_b = childProcess.stdout) === null || _b === void 0 ? void 0 : _b.on('data', (data) => {
	        for (let entry of data.toString().split('\n')) {
	            entry = entry.trim();
	            if (entry) {
	                progress.report(entry);
	            }
	        }
	    });
	    return new Promise((resolve, reject) => {
	        childProcess.on('error', (error) => {
	            reject(error);
	        });
	        childProcess.on('close', (code) => {
	            if (code === 0) {
	                resolve();
	            }
	            else {
	                reject(new Error(`ngcc for ${tsconfig} returned exit code ${code}, stderr: ${stderr.trim()}`));
	            }
	        });
	    });
	}
	exports.resolveAndRunNgcc = resolveAndRunNgcc;

	});

	unwrapExports(ngcc);
	ngcc.resolveAndRunNgcc;

	var session = createCommonjsModule(function (module, exports) {
	/**
	 * @license
	 * Copyright Google Inc. All Rights Reserved.
	 *
	 * Use of this source code is governed by an MIT-style license that can be
	 * found in the LICENSE file at https://angular.io/license
	 */
	Object.defineProperty(exports, "__esModule", { value: true });
	exports.Session = void 0;








	var LanguageId;
	(function (LanguageId) {
	    LanguageId["TS"] = "typescript";
	    LanguageId["HTML"] = "html";
	})(LanguageId || (LanguageId = {}));
	// Empty definition range for files without `scriptInfo`
	const EMPTY_RANGE = node__default['default'].Range.create(0, 0, 0, 0);
	/**
	 * Session is a wrapper around lsp.IConnection, with all the necessary protocol
	 * handlers installed for Angular language service.
	 */
	class Session {
	    constructor(options) {
	        this.configuredProjToExternalProj = new Map();
	        this.diagnosticsTimeout = null;
	        this.isProjectLoading = false;
	        this.logger = options.logger;
	        this.ivy = options.ivy;
	        this.logToConsole = options.logToConsole;
	        // Create a connection for the server. The connection uses Node's IPC as a transport.
	        this.connection = node__default['default'].createConnection();
	        this.addProtocolHandlers(this.connection);
	        this.projectService = this.createProjectService(options);
	    }
	    createProjectService(options) {
	        const projSvc = new tsserverlibrary__default['default'].server.ProjectService({
	            host: options.host,
	            logger: options.logger,
	            cancellationToken: tsserverlibrary__default['default'].server.nullCancellationToken,
	            useSingleInferredProject: true,
	            useInferredProjectPerProjectRoot: true,
	            typingsInstaller: tsserverlibrary__default['default'].server.nullTypingsInstaller,
	            // Not supressing diagnostic events can cause a type error to be thrown when the
	            // language server session gets an event for a file that is outside the project
	            // managed by the project service, and for which a program does not exist in the
	            // corresponding project's language service.
	            // See https://github.com/angular/vscode-ng-language-service/issues/693
	            suppressDiagnosticEvents: true,
	            eventHandler: (e) => this.handleProjectServiceEvent(e),
	            globalPlugins: [options.ngPlugin],
	            pluginProbeLocations: [options.ngProbeLocation],
	            allowLocalPluginLoads: false,
	        });
	        projSvc.setHostConfiguration({
	            formatOptions: projSvc.getHostFormatCodeOptions(),
	            extraFileExtensions: [
	                {
	                    // TODO: in View Engine getExternalFiles() returns a list of external
	                    // templates (HTML files). This configuration is no longer needed in
	                    // Ivy because Ivy returns the typecheck files.
	                    extension: '.html',
	                    isMixedContent: false,
	                    scriptKind: tsserverlibrary__default['default'].ScriptKind.Unknown,
	                },
	            ],
	            preferences: {
	                // We don't want the AutoImportProvider projects to be created. See
	                // https://devblogs.microsoft.com/typescript/announcing-typescript-4-0/#smarter-auto-imports
	                includePackageJsonAutoImports: 'off',
	            },
	        });
	        projSvc.configurePlugin({
	            pluginName: options.ngPlugin,
	            configuration: {
	                angularOnly: true,
	            },
	        });
	        return projSvc;
	    }
	    addProtocolHandlers(conn) {
	        conn.onInitialize(p => this.onInitialize(p));
	        conn.onDidOpenTextDocument(p => this.onDidOpenTextDocument(p));
	        conn.onDidCloseTextDocument(p => this.onDidCloseTextDocument(p));
	        conn.onDidChangeTextDocument(p => this.onDidChangeTextDocument(p));
	        conn.onDidSaveTextDocument(p => this.onDidSaveTextDocument(p));
	        conn.onDefinition(p => this.onDefinition(p));
	        conn.onTypeDefinition(p => this.onTypeDefinition(p));
	        conn.onReferences(p => this.onReferences(p));
	        conn.onHover(p => this.onHover(p));
	        conn.onCompletion(p => this.onCompletion(p));
	        conn.onCompletionResolve(p => this.onCompletionResolve(p));
	    }
	    async runNgcc(configFilePath) {
	        this.connection.sendProgress(progress.NgccProgressType, progress.NgccProgressToken, {
	            done: false,
	            configFilePath,
	            message: `Running ngcc for project ${configFilePath}`,
	        });
	        let success = false;
	        try {
	            await ngcc.resolveAndRunNgcc(configFilePath, {
	                report: (msg) => {
	                    this.connection.sendProgress(progress.NgccProgressType, progress.NgccProgressToken, {
	                        done: false,
	                        configFilePath,
	                        message: msg,
	                    });
	                },
	            });
	            success = true;
	        }
	        catch (e) {
	            this.error(`Failed to run ngcc for ${configFilePath}:\n` +
	                `    ${e.message}\n` +
	                `    Language service will remain disabled.`);
	        }
	        finally {
	            this.connection.sendProgress(progress.NgccProgressType, progress.NgccProgressToken, {
	                done: true,
	                configFilePath,
	                success,
	            });
	        }
	        // Re-enable language service even if ngcc fails, because users could fix
	        // the problem by running ngcc themselves. If we keep language service
	        // disabled, there's no way users could use the extension even after
	        // resolving ngcc issues. On the client side, we will warn users about
	        // potentially degraded experience.
	        this.reenableLanguageServiceForProject(configFilePath);
	    }
	    reenableLanguageServiceForProject(configFilePath) {
	        const project = this.projectService.findProject(configFilePath);
	        if (!project) {
	            this.error(`Failed to find project for ${configFilePath} returned by ngcc.\n` +
	                `Language service will remain disabled.`);
	            return;
	        }
	        project.enableLanguageService();
	        // When the language service got disabled, the program was discarded via
	        // languageService.cleanupSemanticCache(). However, the program is not
	        // recreated when the language service is re-enabled. We manually mark the
	        // project as dirty to force update the graph.
	        project.markAsDirty();
	        this.info(`Enabling Ivy language service for ${project.projectName}.`);
	        // Send diagnostics since we skipped this step when opening the file
	        // (because language service was disabled while waiting for ngcc).
	        // First, make sure the Angular project is complete.
	        this.runGlobalAnalysisForNewlyLoadedProject(project);
	        project.refreshDiagnostics(); // Show initial diagnostics
	    }
	    /**
	     * Invoke the compiler for the first time so that external templates get
	     * matched to the project they belong to.
	     */
	    runGlobalAnalysisForNewlyLoadedProject(project) {
	        if (!project.hasRoots()) {
	            return;
	        }
	        const fileName = project.getRootScriptInfos()[0].fileName;
	        // Getting semantic diagnostics will trigger a global analysis.
	        project.getLanguageService().getSemanticDiagnostics(fileName);
	    }
	    /**
	     * An event handler that gets invoked whenever the program changes and
	     * TS ProjectService sends `ProjectUpdatedInBackgroundEvent`. This particular
	     * event is used to trigger diagnostic checks.
	     * @param event
	     */
	    handleProjectServiceEvent(event) {
	        switch (event.eventName) {
	            case tsserverlibrary__default['default'].server.ProjectLoadingStartEvent:
	                this.isProjectLoading = true;
	                this.connection.sendNotification(notifications.ProjectLoadingStart);
	                this.logger.info(`Loading new project: ${event.data.reason}`);
	                break;
	            case tsserverlibrary__default['default'].server.ProjectLoadingFinishEvent: {
	                if (this.isProjectLoading) {
	                    this.isProjectLoading = false;
	                    this.connection.sendNotification(notifications.ProjectLoadingFinish);
	                }
	                this.checkProject(event.data.project);
	                break;
	            }
	            case tsserverlibrary__default['default'].server.ProjectsUpdatedInBackgroundEvent:
	                // ProjectsUpdatedInBackgroundEvent is sent whenever diagnostics are
	                // requested via project.refreshDiagnostics()
	                this.triggerDiagnostics(event.data.openFiles);
	                break;
	            case tsserverlibrary__default['default'].server.ProjectLanguageServiceStateEvent:
	                this.connection.sendNotification(notifications.ProjectLanguageService, {
	                    projectName: event.data.project.getProjectName(),
	                    languageServiceEnabled: event.data.languageServiceEnabled,
	                });
	        }
	    }
	    /**
	     * Retrieve Angular diagnostics for the specified `openFiles` after a specific
	     * `delay`, or renew the request if there's already a pending one.
	     * @param openFiles
	     * @param delay time to wait before sending request (milliseconds)
	     */
	    triggerDiagnostics(openFiles, delay = 200) {
	        // Do not immediately send a diagnostics request. Send only after user has
	        // stopped typing after the specified delay.
	        if (this.diagnosticsTimeout) {
	            // If there's an existing timeout, cancel it
	            clearTimeout(this.diagnosticsTimeout);
	        }
	        // Set a new timeout
	        this.diagnosticsTimeout = setTimeout(() => {
	            this.diagnosticsTimeout = null; // clear the timeout
	            this.sendPendingDiagnostics(openFiles);
	            // Default delay is 200ms, consistent with TypeScript. See
	            // https://github.com/microsoft/vscode/blob/7b944a16f52843b44cede123dd43ae36c0405dfd/extensions/typescript-language-features/src/features/bufferSyncSupport.ts#L493)
	        }, delay);
	    }
	    /**
	     * Execute diagnostics request for each of the specified `openFiles`.
	     * @param openFiles
	     */
	    sendPendingDiagnostics(openFiles) {
	        for (const fileName of openFiles) {
	            const result = this.getLSAndScriptInfo(fileName);
	            if (!result) {
	                continue;
	            }
	            const diagnostics = result.languageService.getSemanticDiagnostics(fileName);
	            // Need to send diagnostics even if it's empty otherwise editor state will
	            // not be updated.
	            this.connection.sendDiagnostics({
	                uri: utils.filePathToUri(fileName),
	                diagnostics: diagnostics.map(d => diagnostic.tsDiagnosticToLspDiagnostic(d, result.scriptInfo)),
	            });
	        }
	    }
	    /**
	     * Return the default project for the specified `scriptInfo` if it is already
	     * a configured project. If not, attempt to find a relevant config file and
	     * make that project its default. This method is to ensure HTML files always
	     * belong to a configured project instead of the default behavior of being in
	     * an inferred project.
	     * @param scriptInfo
	     */
	    getDefaultProjectForScriptInfo(scriptInfo) {
	        let project = this.projectService.getDefaultProjectForFile(scriptInfo.fileName, 
	        // ensureProject tries to find a default project for the scriptInfo if
	        // it does not already have one. It is not needed here because we are
	        // going to assign it a project below if it does not have one.
	        false // ensureProject
	        );
	        // TODO: verify that HTML files are attached to Inferred project by default.
	        // If they are already part of a ConfiguredProject then the following is
	        // not needed.
	        if (!project || project.projectKind !== tsserverlibrary__default['default'].server.ProjectKind.Configured) {
	            const { configFileName } = this.projectService.openClientFile(scriptInfo.fileName);
	            if (!configFileName) {
	                // Failed to find a config file. There is nothing we could do.
	                this.error(`No config file for ${scriptInfo.fileName}`);
	                return;
	            }
	            project = this.projectService.findProject(configFileName);
	            if (!project) {
	                return;
	            }
	            scriptInfo.detachAllProjects();
	            scriptInfo.attachToProject(project);
	        }
	        this.createExternalProject(project);
	        return project;
	    }
	    onInitialize(params) {
	        const serverOptions = {
	            logFile: this.logger.getLogFileName(),
	        };
	        return {
	            capabilities: {
	                textDocumentSync: node__default['default'].TextDocumentSyncKind.Incremental,
	                completionProvider: {
	                    // Only the Ivy LS provides support for additional completion resolution.
	                    resolveProvider: this.ivy,
	                    triggerCharacters: ['<', '.', '*', '[', '(', '$', '|']
	                },
	                definitionProvider: true,
	                typeDefinitionProvider: this.ivy,
	                referencesProvider: this.ivy,
	                hoverProvider: true,
	                workspace: {
	                    workspaceFolders: { supported: true },
	                },
	            },
	            serverOptions,
	        };
	    }
	    onDidOpenTextDocument(params) {
	        var _a;
	        const { uri, languageId, text } = params.textDocument;
	        const filePath = utils.uriToFilePath(uri);
	        if (!filePath) {
	            return;
	        }
	        // External templates (HTML files) should be tagged as ScriptKind.Unknown
	        // so that they don't get parsed as TS files. See
	        // https://github.com/microsoft/TypeScript/blob/b217f22e798c781f55d17da72ed099a9dee5c650/src/compiler/program.ts#L1897-L1899
	        const scriptKind = languageId === LanguageId.TS ? tsserverlibrary__default['default'].ScriptKind.TS : tsserverlibrary__default['default'].ScriptKind.Unknown;
	        try {
	            // The content could be newer than that on disk. This could be due to
	            // buffer in the user's editor which has not been saved to disk.
	            // See https://github.com/angular/vscode-ng-language-service/issues/632
	            const result = this.projectService.openClientFile(filePath, text, scriptKind);
	            const { configFileName, configFileErrors } = result;
	            if (configFileErrors && configFileErrors.length) {
	                // configFileErrors is an empty array even if there's no error, so check length.
	                this.error(configFileErrors.map(e => e.messageText).join('\n'));
	            }
	            const project = configFileName ?
	                this.projectService.findProject(configFileName) : (_a = this.projectService.getScriptInfo(filePath)) === null || _a === void 0 ? void 0 : _a.containingProjects.find(utils.isConfiguredProject);
	            if (!project) {
	                return;
	            }
	            if (project.languageServiceEnabled) {
	                project.refreshDiagnostics(); // Show initial diagnostics
	            }
	        }
	        catch (error) {
	            if (this.isProjectLoading) {
	                this.isProjectLoading = false;
	                this.connection.sendNotification(notifications.ProjectLoadingFinish);
	            }
	            if (error.stack) {
	                this.error(error.stack);
	            }
	            throw error;
	        }
	    }
	    /**
	     * Creates an external project with the same config path as `project` so that TypeScript keeps the
	     * project open when navigating away from `html` files.
	     */
	    createExternalProject(project) {
	        if (utils.isConfiguredProject(project) &&
	            !this.configuredProjToExternalProj.has(project.projectName)) {
	            const extProjectName = `${project.projectName}-external`;
	            project.projectService.openExternalProject({
	                projectFileName: extProjectName,
	                rootFiles: [{ fileName: project.getConfigFilePath() }],
	                options: {}
	            });
	            this.configuredProjToExternalProj.set(project.projectName, extProjectName);
	        }
	    }
	    onDidCloseTextDocument(params) {
	        const { textDocument } = params;
	        const filePath = utils.uriToFilePath(textDocument.uri);
	        if (!filePath) {
	            return;
	        }
	        this.projectService.closeClientFile(filePath);
	        this.maybeCloseExternalProject(filePath);
	    }
	    /**
	     * We open external projects for files so that we can prevent TypeScript from closing a project
	     * when there is open external HTML template that still references the project. This function
	     * checks if there are no longer any open files in the project for the given `filePath`. If there
	     * aren't, we also close the external project that was created.
	     */
	    maybeCloseExternalProject(filePath) {
	        const scriptInfo = this.projectService.getScriptInfo(filePath);
	        if (!scriptInfo) {
	            return;
	        }
	        for (const [configuredProjName, externalProjName] of this.configuredProjToExternalProj) {
	            const configuredProj = this.projectService.findProject(configuredProjName);
	            if (!configuredProj || configuredProj.isClosed()) {
	                this.projectService.closeExternalProject(externalProjName);
	                this.configuredProjToExternalProj.delete(configuredProjName);
	                continue;
	            }
	            // See if any openFiles belong to the configured project.
	            // if not, close external project this.projectService.openFiles
	            const openFiles = toArray(this.projectService.openFiles.keys());
	            if (!openFiles.some(file => {
	                const scriptInfo = this.projectService.getScriptInfo(file);
	                return scriptInfo === null || scriptInfo === void 0 ? void 0 : scriptInfo.isAttached(configuredProj);
	            })) {
	                this.projectService.closeExternalProject(externalProjName);
	                this.configuredProjToExternalProj.delete(configuredProjName);
	            }
	        }
	    }
	    onDidChangeTextDocument(params) {
	        const { contentChanges, textDocument } = params;
	        const filePath = utils.uriToFilePath(textDocument.uri);
	        if (!filePath) {
	            return;
	        }
	        const scriptInfo = this.projectService.getScriptInfo(filePath);
	        if (!scriptInfo) {
	            this.error(`Failed to get script info for ${filePath}`);
	            return;
	        }
	        for (const change of contentChanges) {
	            if ('range' in change) {
	                const [start, end] = utils.lspRangeToTsPositions(scriptInfo, change.range);
	                scriptInfo.editContent(start, end, change.text);
	            }
	            else {
	                // New text is considered to be the full content of the document.
	                scriptInfo.editContent(0, scriptInfo.getSnapshot().getLength(), change.text);
	            }
	        }
	        const project = this.getDefaultProjectForScriptInfo(scriptInfo);
	        if (!project || !project.languageServiceEnabled) {
	            return;
	        }
	        project.refreshDiagnostics();
	    }
	    onDidSaveTextDocument(params) {
	        const { text, textDocument } = params;
	        const filePath = utils.uriToFilePath(textDocument.uri);
	        const scriptInfo = this.projectService.getScriptInfo(filePath);
	        if (!scriptInfo) {
	            return;
	        }
	        if (text) {
	            scriptInfo.open(text);
	        }
	        else {
	            scriptInfo.reloadFromFile();
	        }
	    }
	    onDefinition(params) {
	        const lsInfo = this.getLSAndScriptInfo(params.textDocument);
	        if (lsInfo === undefined) {
	            return;
	        }
	        const { languageService, scriptInfo } = lsInfo;
	        const offset = utils.lspPositionToTsPosition(scriptInfo, params.position);
	        const definition = languageService.getDefinitionAndBoundSpan(scriptInfo.fileName, offset);
	        if (!definition || !definition.definitions) {
	            return;
	        }
	        const originSelectionRange = utils.tsTextSpanToLspRange(scriptInfo, definition.textSpan);
	        return this.tsDefinitionsToLspLocationLinks(definition.definitions, originSelectionRange);
	    }
	    onTypeDefinition(params) {
	        const lsInfo = this.getLSAndScriptInfo(params.textDocument);
	        if (lsInfo === undefined) {
	            return;
	        }
	        const { languageService, scriptInfo } = lsInfo;
	        const offset = utils.lspPositionToTsPosition(scriptInfo, params.position);
	        const definitions = languageService.getTypeDefinitionAtPosition(scriptInfo.fileName, offset);
	        if (!definitions) {
	            return;
	        }
	        return this.tsDefinitionsToLspLocationLinks(definitions);
	    }
	    onReferences(params) {
	        const lsInfo = this.getLSAndScriptInfo(params.textDocument);
	        if (lsInfo === undefined) {
	            return;
	        }
	        const { languageService, scriptInfo } = lsInfo;
	        const offset = utils.lspPositionToTsPosition(scriptInfo, params.position);
	        const references = languageService.getReferencesAtPosition(scriptInfo.fileName, offset);
	        if (references === undefined) {
	            return;
	        }
	        return references.map(ref => {
	            const scriptInfo = this.projectService.getScriptInfo(ref.fileName);
	            const range = scriptInfo ? utils.tsTextSpanToLspRange(scriptInfo, ref.textSpan) : EMPTY_RANGE;
	            const uri = utils.filePathToUri(ref.fileName);
	            return { uri, range };
	        });
	    }
	    tsDefinitionsToLspLocationLinks(definitions, originSelectionRange) {
	        const results = [];
	        for (const d of definitions) {
	            const scriptInfo = this.projectService.getScriptInfo(d.fileName);
	            // Some definitions, like definitions of CSS files, may not be recorded files with a
	            // `scriptInfo` but are still valid definitions because they are files that exist. In this
	            // case, check to make sure that the text span of the definition is zero so that the file
	            // doesn't have to be read; if the span is non-zero, we can't do anything with this
	            // definition.
	            if (!scriptInfo && d.textSpan.length > 0) {
	                continue;
	            }
	            const range = scriptInfo ? utils.tsTextSpanToLspRange(scriptInfo, d.textSpan) : EMPTY_RANGE;
	            const targetUri = utils.filePathToUri(d.fileName);
	            results.push({
	                originSelectionRange,
	                targetUri,
	                targetRange: range,
	                targetSelectionRange: range,
	            });
	        }
	        return results;
	    }
	    getLSAndScriptInfo(textDocumentOrFileName) {
	        const filePath = node__default['default'].TextDocumentIdentifier.is(textDocumentOrFileName) ?
	            utils.uriToFilePath(textDocumentOrFileName.uri) :
	            textDocumentOrFileName;
	        const scriptInfo = this.projectService.getScriptInfo(filePath);
	        if (!scriptInfo) {
	            this.error(`Script info not found for ${filePath}`);
	            return;
	        }
	        const project = this.getDefaultProjectForScriptInfo(scriptInfo);
	        if (!(project === null || project === void 0 ? void 0 : project.languageServiceEnabled)) {
	            return;
	        }
	        if (project.isClosed()) {
	            scriptInfo.detachFromProject(project);
	            this.logger.info(`Failed to get language service for closed project ${project.projectName}.`);
	            return undefined;
	        }
	        return {
	            languageService: project.getLanguageService(),
	            scriptInfo,
	        };
	    }
	    onHover(params) {
	        const lsInfo = this.getLSAndScriptInfo(params.textDocument);
	        if (lsInfo === undefined) {
	            return;
	        }
	        const { languageService, scriptInfo } = lsInfo;
	        const offset = utils.lspPositionToTsPosition(scriptInfo, params.position);
	        const info = languageService.getQuickInfoAtPosition(scriptInfo.fileName, offset);
	        if (!info) {
	            return;
	        }
	        const { kind, kindModifiers, textSpan, displayParts, documentation } = info;
	        let desc = kindModifiers ? kindModifiers + ' ' : '';
	        if (displayParts) {
	            // displayParts does not contain info about kindModifiers
	            // but displayParts does contain info about kind
	            desc += displayParts.map(dp => dp.text).join('');
	        }
	        else {
	            desc += kind;
	        }
	        const contents = [{
	                language: 'typescript',
	                value: desc,
	            }];
	        if (documentation) {
	            for (const d of documentation) {
	                contents.push(d.text);
	            }
	        }
	        return {
	            contents,
	            range: utils.tsTextSpanToLspRange(scriptInfo, textSpan),
	        };
	    }
	    onCompletion(params) {
	        const lsInfo = this.getLSAndScriptInfo(params.textDocument);
	        if (lsInfo === undefined) {
	            return;
	        }
	        const { languageService, scriptInfo } = lsInfo;
	        const offset = utils.lspPositionToTsPosition(scriptInfo, params.position);
	        const completions = languageService.getCompletionsAtPosition(scriptInfo.fileName, offset, {
	        // options
	        });
	        if (!completions) {
	            return;
	        }
	        return completions.entries.map((e) => completion.tsCompletionEntryToLspCompletionItem(e, params.position, scriptInfo));
	    }
	    onCompletionResolve(item) {
	        var _a;
	        const data = completion.readNgCompletionData(item);
	        if (data === null) {
	            // This item wasn't tagged with Angular LS completion data - it probably didn't originate
	            // from this language service.
	            return item;
	        }
	        const { filePath, position } = data;
	        const lsInfo = this.getLSAndScriptInfo(filePath);
	        if (lsInfo === undefined) {
	            return item;
	        }
	        const { languageService, scriptInfo } = lsInfo;
	        const offset = utils.lspPositionToTsPosition(scriptInfo, position);
	        const details = languageService.getCompletionEntryDetails(filePath, offset, (_a = item.insertText) !== null && _a !== void 0 ? _a : item.label, undefined, undefined, undefined);
	        if (details === undefined) {
	            return item;
	        }
	        const { kind, kindModifiers, displayParts, documentation } = details;
	        let desc = kindModifiers ? kindModifiers + ' ' : '';
	        if (displayParts) {
	            // displayParts does not contain info about kindModifiers
	            // but displayParts does contain info about kind
	            desc += displayParts.map(dp => dp.text).join('');
	        }
	        else {
	            desc += kind;
	        }
	        item.detail = desc;
	        item.documentation = documentation === null || documentation === void 0 ? void 0 : documentation.map(d => d.text).join('');
	        return item;
	    }
	    /**
	     * Show an error message in the remote console and log to file.
	     *
	     * @param message The message to show.
	     */
	    error(message) {
	        if (this.logToConsole) {
	            this.connection.console.error(message);
	        }
	        this.logger.msg(message, tsserverlibrary__default['default'].server.Msg.Err);
	    }
	    /**
	     * Show a warning message in the remote console and log to file.
	     *
	     * @param message The message to show.
	     */
	    warn(message) {
	        if (this.logToConsole) {
	            this.connection.console.warn(message);
	        }
	        // ts.server.Msg does not have warning level, so log as info.
	        this.logger.msg(`[WARN] ${message}`, tsserverlibrary__default['default'].server.Msg.Info);
	    }
	    /**
	     * Show an information message in the remote console and log to file.
	     *
	     * @param message The message to show.
	     */
	    info(message) {
	        if (this.logToConsole) {
	            this.connection.console.info(message);
	        }
	        this.logger.msg(message, tsserverlibrary__default['default'].server.Msg.Info);
	    }
	    /**
	     * Start listening on the input stream for messages to process.
	     */
	    listen() {
	        this.connection.listen();
	    }
	    /**
	     * Disable the language service if the specified `project` is not Angular or
	     * Ivy mode is enabled.
	     */
	    checkProject(project) {
	        const { projectName } = project;
	        if (!project.languageServiceEnabled) {
	            this.info(`Language service is already disabled for ${projectName}. ` +
	                `This could be due to non-TS files that exceeded the size limit (${tsserverlibrary__default['default'].server.maxProgramSizeForNonTsFiles} bytes).` +
	                `Please check log file for details.`);
	            return;
	        }
	        if (!this.checkIsAngularProject(project)) {
	            return;
	        }
	        if (this.ivy && utils.isConfiguredProject(project)) {
	            // Keep language service disabled until ngcc is completed.
	            project.disableLanguageService();
	            // Do not wait on this promise otherwise we'll be blocking other requests
	            this.runNgcc(project.getConfigFilePath()).catch((error) => {
	                this.error(error.toString());
	            });
	        }
	        else {
	            // Immediately enable Legacy/ViewEngine language service
	            this.info(`Enabling VE language service for ${projectName}.`);
	        }
	    }
	    /**
	     * Determine if the specified `project` is Angular, and disable the language
	     * service if not.
	     */
	    checkIsAngularProject(project) {
	        const { projectName } = project;
	        const NG_CORE = '@angular/core/core.d.ts';
	        const isAngularProject = project.hasRoots() && !project.isNonTsProject() &&
	            project.getFileNames().some(f => f.endsWith(NG_CORE));
	        if (isAngularProject) {
	            return true;
	        }
	        project.disableLanguageService();
	        this.info(`Disabling language service for ${projectName} because it is not an Angular project ` +
	            `('${NG_CORE}' could not be found). ` +
	            `If you believe you are seeing this message in error, please reinstall the packages in your package.json.`);
	        if (project.getExcludedFiles().some(f => f.endsWith(NG_CORE))) {
	            this.info(`Please check your tsconfig.json to make sure 'node_modules' directory is not excluded.`);
	        }
	        return false;
	    }
	}
	exports.Session = Session;
	function toArray(it) {
	    const results = [];
	    for (let itResult = it.next(); !itResult.done; itResult = it.next()) {
	        results.push(itResult.value);
	    }
	    return results;
	}

	});

	unwrapExports(session);
	session.Session;

	var version_provider = createCommonjsModule(function (module, exports) {
	/**
	 * @license
	 * Copyright Google Inc. All Rights Reserved.
	 *
	 * Use of this source code is governed by an MIT-style license that can be
	 * found in the LICENSE file at https://angular.io/license
	 */
	Object.defineProperty(exports, "__esModule", { value: true });
	exports.Version = exports.resolveNgLangSvc = exports.resolveTsServer = void 0;

	const MIN_TS_VERSION = '4.1';
	const MIN_NG_VERSION = '11.1';
	function resolve(packageName, location, rootPackage) {
	    rootPackage = rootPackage || packageName;
	    try {
	        const packageJsonPath = require.resolve(`${rootPackage}/package.json`, {
	            paths: [location],
	        });
	        // Do not use require() to read JSON files since it's a potential security
	        // vulnerability.
	        const packageJson = JSON.parse(fs__default['default'].readFileSync(packageJsonPath, 'utf8'));
	        const resolvedPath = require.resolve(packageName, {
	            paths: [location],
	        });
	        return {
	            name: packageName,
	            resolvedPath,
	            version: new Version(packageJson.version),
	        };
	    }
	    catch (_a) {
	    }
	}
	/**
	 * Resolve the node module with the specified `packageName` that satisfies
	 * the specified minimum version.
	 * @param packageName name of package to be resolved
	 * @param minVersionStr minimum version
	 * @param probeLocations locations to initiate node module resolution
	 * @param rootPackage location of package.json. For example, the root package of
	 * `typescript/lib/tsserverlibrary` is `typescript`.
	 */
	function resolveWithMinVersion(packageName, minVersionStr, probeLocations, rootPackage) {
	    if (!packageName.startsWith(rootPackage)) {
	        throw new Error(`${packageName} must be in the root package`);
	    }
	    const minVersion = new Version(minVersionStr);
	    for (const location of probeLocations) {
	        const nodeModule = resolve(packageName, location, rootPackage);
	        if (nodeModule && nodeModule.version.greaterThanOrEqual(minVersion)) {
	            return nodeModule;
	        }
	    }
	    throw new Error(`Failed to resolve '${packageName}' with minimum version '${minVersion}' from ` +
	        JSON.stringify(probeLocations, null, 2));
	}
	/**
	 * Resolve `typescript/lib/tsserverlibrary` from the given locations.
	 * @param probeLocations
	 */
	function resolveTsServer(probeLocations) {
	    const tsserver = 'typescript/lib/tsserverlibrary';
	    return resolveWithMinVersion(tsserver, MIN_TS_VERSION, probeLocations, 'typescript');
	}
	exports.resolveTsServer = resolveTsServer;
	/**
	 * Resolve `@angular/language-service` from the given locations.
	 * @param probeLocations locations from which resolution is attempted
	 * @param ivy true if Ivy language service is requested
	 */
	function resolveNgLangSvc(probeLocations, ivy) {
	    const nglangsvc = '@angular/language-service';
	    const packageName = ivy ? `${nglangsvc}/bundles/ivy` : nglangsvc;
	    return resolveWithMinVersion(packageName, MIN_NG_VERSION, probeLocations, nglangsvc);
	}
	exports.resolveNgLangSvc = resolveNgLangSvc;
	/**
	 * Converts the specified string `a` to non-negative integer.
	 * Returns -1 if the result is NaN.
	 * @param a
	 */
	function parseNonNegativeInt(a) {
	    // parseInt() will try to convert as many as possible leading characters that
	    // are digits. This means a string like "123abc" will be converted to 123.
	    // For our use case, this is sufficient.
	    const i = parseInt(a, 10 /* radix */);
	    return isNaN(i) ? -1 : i;
	}
	class Version {
	    constructor(versionStr) {
	        this.versionStr = versionStr;
	        const [major, minor, patch] = Version.parseVersionStr(versionStr);
	        this.major = major;
	        this.minor = minor;
	        this.patch = patch;
	    }
	    greaterThanOrEqual(other) {
	        if (this.major < other.major) {
	            return false;
	        }
	        if (this.major > other.major) {
	            return true;
	        }
	        if (this.minor < other.minor) {
	            return false;
	        }
	        if (this.minor > other.minor) {
	            return true;
	        }
	        return this.patch >= other.patch;
	    }
	    toString() {
	        return this.versionStr;
	    }
	    /**
	     * Converts the specified `versionStr` to its number constituents. Invalid
	     * number value is represented as negative number.
	     * @param versionStr
	     */
	    static parseVersionStr(versionStr) {
	        const [major, minor, patch] = versionStr.split('.').map(parseNonNegativeInt);
	        return [
	            major === undefined ? 0 : major,
	            minor === undefined ? 0 : minor,
	            patch === undefined ? 0 : patch,
	        ];
	    }
	}
	exports.Version = Version;

	});

	unwrapExports(version_provider);
	version_provider.Version;
	version_provider.resolveNgLangSvc;
	version_provider.resolveTsServer;

	var server = createCommonjsModule(function (module, exports) {
	/**
	 * @license
	 * Copyright Google Inc. All Rights Reserved.
	 *
	 * Use of this source code is governed by an MIT-style license that can be
	 * found in the LICENSE file at https://angular.io/license
	 */
	Object.defineProperty(exports, "__esModule", { value: true });





	// Parse command line arguments
	const options = cmdline_utils.parseCommandLine(process.argv);
	if (options.help) {
	    console.error(cmdline_utils.generateHelpMessage(process.argv));
	    process.exit(0);
	}
	// Create a logger that logs to file. OK to emit verbose entries.
	const logger$1 = logger.createLogger({
	    logFile: options.logFile,
	    logVerbosity: options.logVerbosity,
	});
	const ts = version_provider.resolveTsServer(options.tsProbeLocations);
	const ng = version_provider.resolveNgLangSvc(options.ngProbeLocations, options.ivy);
	// ServerHost provides native OS functionality
	const host = new server_host.ServerHost();
	// Establish a new server session that encapsulates lsp connection.
	const session$1 = new session.Session({
	    host,
	    logger: logger$1,
	    ngPlugin: ng.name,
	    ngProbeLocation: ng.resolvedPath,
	    ivy: options.ivy,
	    logToConsole: options.logToConsole,
	});
	// Log initialization info
	session$1.info(`Angular language server process ID: ${process.pid}`);
	session$1.info(`Using ${ts.name} v${ts.version} from ${ts.resolvedPath}`);
	session$1.info(`Using ${ng.name} v${ng.version} from ${ng.resolvedPath}`);
	session$1.info(`Log file: ${logger$1.getLogFileName()}`);
	if (process.env.NG_DEBUG === 'true') {
	    session$1.info('Angular Language Service is running under DEBUG mode');
	}
	if (process.env.TSC_NONPOLLING_WATCHER !== 'true') {
	    session$1.warn(`Using less efficient polling watcher. Set TSC_NONPOLLING_WATCHER to true.`);
	}
	session$1.listen();

	});

	var server$1 = unwrapExports(server);

	return server$1;

});
