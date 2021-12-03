/* --------------------------------------------------------------------------------------------
 * Copyright (c) Microsoft Corporation. All rights reserved.
 * Licensed under the MIT License. See License.txt in the project root for license information.
 * ------------------------------------------------------------------------------------------ */
'use strict';
Object.defineProperty(exports, "__esModule", { value: true });
const assert = require("assert");
const stream_1 = require("stream");
const main_1 = require("../main");
const main_2 = require("../../../../textDocument/lib/umd/main");
class TestStream extends stream_1.Duplex {
    _write(chunk, _encoding, done) {
        this.emit('data', chunk);
        done();
    }
    _read(_size) { }
}
const TEST_URI = 'file:///my/path/to/my-file.ts';
const TEST_LANGUAGE_ID = 'typescript';
function mockOpenDocNotif(config) {
    const { version, text } = config;
    return { textDocument: { uri: TEST_URI, languageId: TEST_LANGUAGE_ID, version, text } };
}
function mockChangeDocNotif(config) {
    const { version, contentChanges } = config;
    return { textDocument: { uri: TEST_URI, version }, contentChanges };
}
suite('Legacy standard text documents', () => {
    let server;
    let client;
    setup(() => {
        const up = new TestStream();
        const down = new TestStream();
        server = main_1.createConnection(up, down);
        client = main_1.createConnection(down, up);
        server.listen();
        client.listen();
    });
    test('Change full file content', done => {
        const textDocs = new main_1.TextDocuments(main_2.TextDocument);
        textDocs.listen(server);
        textDocs.onDidChangeContent(event => {
            if (event.document.version === 2) {
                assert.equal(event.document.getText(), 'efg456');
                done();
            }
        });
        const openDocNotif = mockOpenDocNotif({ text: 'abc123', version: 1 });
        client.sendNotification(main_1.DidOpenTextDocumentNotification.type, openDocNotif);
        const changeDocNotif = mockChangeDocNotif({ contentChanges: [{ text: 'efg456' }], version: 2 });
        client.sendNotification(main_1.DidChangeTextDocumentNotification.type, changeDocNotif);
    });
    test('Several full content updates', done => {
        const textDocs = new main_1.TextDocuments(main_2.TextDocument);
        textDocs.listen(server);
        textDocs.onDidChangeContent(event => {
            if (event.document.version === 2) {
                assert.equal(event.document.getText(), 'world');
                done();
            }
        });
        const openDocNotif = mockOpenDocNotif({ text: 'abc123', version: 1 });
        client.sendNotification(main_1.DidOpenTextDocumentNotification.type, openDocNotif);
        const changeDocNotif = mockChangeDocNotif({ contentChanges: [{ text: 'hello' }, { text: 'world' }], version: 2 });
        client.sendNotification(main_1.DidChangeTextDocumentNotification.type, changeDocNotif);
    });
});
suite('New custom text Documents', () => {
    let server;
    let client;
    setup(() => {
        const up = new TestStream();
        const down = new TestStream();
        server = main_1.createConnection(up, down);
        client = main_1.createConnection(down, up);
        server.listen();
        client.listen();
    });
    test('Incrementally removing content', done => {
        const textDocs = new main_1.TextDocuments(main_2.TextDocument);
        textDocs.listen(server);
        textDocs.onDidChangeContent(event => {
            if (event.document.version === 2) {
                assert.equal(event.document.getText(), 'function abc() {\n  console.log("hello");\n}');
                done();
            }
        });
        const openDocNotif = mockOpenDocNotif({ version: 1, text: 'function abc() {\n  console.log("hello, world!");\n}' });
        client.sendNotification(main_1.DidOpenTextDocumentNotification.type, openDocNotif);
        const changeDocNotif = mockChangeDocNotif({ version: 2, contentChanges: [{ text: 'hello', range: main_1.Range.create(1, 15, 1, 28) }] });
        client.sendNotification(main_1.DidChangeTextDocumentNotification.type, changeDocNotif);
    });
    test('Incrementally adding content', done => {
        const textDocs = new main_1.TextDocuments(main_2.TextDocument);
        textDocs.listen(server);
        textDocs.onDidChangeContent(event => {
            if (event.document.version === 2) {
                assert.equal(event.document.getText(), 'function abc() {\n  console.log("hello, world!");\n}');
                done();
            }
        });
        const openDocNotif = mockOpenDocNotif({ version: 1, text: 'function abc() {\n  console.log("hello");\n}' });
        client.sendNotification(main_1.DidOpenTextDocumentNotification.type, openDocNotif);
        const changeDocNotif = mockChangeDocNotif({ version: 2, contentChanges: [{ text: ', world!', range: main_1.Range.create(1, 20, 1, 20) }] });
        client.sendNotification(main_1.DidChangeTextDocumentNotification.type, changeDocNotif);
    });
    test('Incrementally replacing content', done => {
        const textDocs = new main_1.TextDocuments(main_2.TextDocument);
        textDocs.listen(server);
        textDocs.onDidChangeContent(event => {
            if (event.document.version === 2) {
                assert.equal(event.document.getText(), 'function abc() {\n  console.log("hello, test case!!!");\n}');
                done();
            }
        });
        const openDocNotif = mockOpenDocNotif({ version: 1, text: 'function abc() {\n  console.log("hello, world!");\n}' });
        client.sendNotification(main_1.DidOpenTextDocumentNotification.type, openDocNotif);
        const changeDocNotif = mockChangeDocNotif({ version: 2, contentChanges: [{ text: 'hello, test case!!!', range: main_1.Range.create(1, 15, 1, 28) }] });
        client.sendNotification(main_1.DidChangeTextDocumentNotification.type, changeDocNotif);
    });
    test('Several incremental content changes', done => {
        const textDocs = new main_1.TextDocuments(main_2.TextDocument);
        textDocs.listen(server);
        textDocs.onDidChangeContent(event => {
            if (event.document.version === 2) {
                assert.equal(event.document.getText(), 'function abcdefghij() {\n  console.log("hello, test case!!!");\n}');
                done();
            }
        });
        const openDocNotif = mockOpenDocNotif({ version: 1, text: 'function abc() {\n  console.log("hello, world!");\n}' });
        client.sendNotification(main_1.DidOpenTextDocumentNotification.type, openDocNotif);
        const changeDocNotif = mockChangeDocNotif({
            version: 2,
            contentChanges: [
                { text: 'defg', range: main_1.Range.create(0, 12, 0, 12) },
                { text: 'hello, test case!!!', range: main_1.Range.create(1, 15, 1, 28) },
                { text: 'hij', range: main_1.Range.create(0, 16, 0, 16) },
            ]
        });
        client.sendNotification(main_1.DidChangeTextDocumentNotification.type, changeDocNotif);
    });
});
//# sourceMappingURL=textdocuments.test.js.map