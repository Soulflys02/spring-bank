import { HttpErrorResponse } from '@angular/common/http';
import { TestBed } from '@angular/core/testing';
import Keycloak from 'keycloak-js';
import { of, throwError } from 'rxjs';
import type { Mock } from 'vitest';
import { App } from './app';
import { Customer } from './customer/customer';
import { CustomerApi } from './customer/customer-api';

describe('App', () => {
  let keycloak: { tokenParsed: { name: string }; logout: Mock; accountManagement: Mock };
  let customerApi: { getMe: Mock; create: Mock };

  const customer: Customer = {
    firstName: 'Ada',
    lastName: 'Lovelace',
    email: 'ada.lovelace@example.com',
  };

  beforeEach(async () => {
    keycloak = {
      tokenParsed: { name: 'Client Test' },
      logout: vi.fn(),
      accountManagement: vi.fn(),
    };
    customerApi = {
      getMe: vi.fn().mockReturnValue(of(customer)),
      create: vi.fn().mockReturnValue(of(customer)),
    };

    await TestBed.configureTestingModule({
      imports: [App],
      providers: [
        { provide: Keycloak, useValue: keycloak },
        { provide: CustomerApi, useValue: customerApi },
      ],
    }).compileComponents();
  });

  it('should create the app', () => {
    const fixture = TestBed.createComponent(App);
    expect(fixture.componentInstance).toBeTruthy();
  });

  it('should show the user name', async () => {
    const fixture = TestBed.createComponent(App);
    await fixture.whenStable();

    expect(fixture.nativeElement.textContent).toContain('Client Test');
  });

  it('should log out through Keycloak', async () => {
    const fixture = TestBed.createComponent(App);
    await fixture.whenStable();

    fixture.nativeElement.querySelector('[data-testid="logout"]').click();

    expect(keycloak.logout).toHaveBeenCalledWith({ redirectUri: window.location.origin });
  });

  it('should open the profile in Keycloak', async () => {
    const fixture = TestBed.createComponent(App);
    await fixture.whenStable();

    fixture.nativeElement.querySelector('[data-testid="profile"]').click();

    expect(keycloak.accountManagement).toHaveBeenCalled();
  });

  it('should not create an existing customer', async () => {
    const fixture = TestBed.createComponent(App);
    await fixture.whenStable();

    expect(customerApi.getMe).toHaveBeenCalledOnce();
    expect(customerApi.create).not.toHaveBeenCalled();
  });

  it('should create a customer who does not exist yet', async () => {
    customerApi.getMe.mockReturnValue(
      throwError(() => new HttpErrorResponse({ status: 404, statusText: 'Not Found' })),
    );

    const fixture = TestBed.createComponent(App);
    await fixture.whenStable();

    expect(customerApi.create).toHaveBeenCalledOnce();
  });

  it('should not create the customer on another error', async () => {
    customerApi.getMe.mockReturnValue(
      throwError(() => new HttpErrorResponse({ status: 500, statusText: 'Server Error' })),
    );

    const fixture = TestBed.createComponent(App);
    await fixture.whenStable();

    expect(customerApi.create).not.toHaveBeenCalled();
  });
});
